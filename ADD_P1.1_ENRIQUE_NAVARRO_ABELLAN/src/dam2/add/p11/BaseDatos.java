package dam2.add.p11;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;


public class BaseDatos {
	private File fileUsuarios;
	private File fileAccesos;
	private File file_usuarios_temp;
	private File file_accesos_temp;
	private File fileLog;
		
	public BaseDatos(File fileUsuarios, File fileAccesos, File file_usuarios_temp, File file_accesos_temp,
			File fileLog) {
		super();
		this.fileUsuarios = fileUsuarios;
		this.fileAccesos = fileAccesos;
		this.file_usuarios_temp = file_usuarios_temp;
		this.file_accesos_temp = file_accesos_temp;
		this.fileLog = fileLog;
	}

	public File getFileUsuarios() {
		return fileUsuarios;
	}

	public void setFileUsuarios(File fileUsuarios) {
		this.fileUsuarios = fileUsuarios;
	}

	public File getFileAccesos() {
		return fileAccesos;
	}

	public void setFileAccesos(File fileAccesos) {
		this.fileAccesos = fileAccesos;
	}

	public File getFile_usuarios_temp() {
		return file_usuarios_temp;
	}

	public void setFile_usuarios_temp(File file_usuarios_temp) {
		this.file_usuarios_temp = file_usuarios_temp;
	}

	public File getFile_accesos_temp() {
		return file_accesos_temp;
	}

	public void setFile_accesos_temp(File file_accesos_temp) {
		this.file_accesos_temp = file_accesos_temp;
	}

	public File getFileLog() {
		return fileLog;
	}

	public void setFileLog(File fileLog) {
		this.fileLog = fileLog;
	}

	public static HashMap<String, HashMap> recargarUsuarios (BaseDatos base_datos) {
		HashMap<String, HashMap> bdd = new HashMap<String, HashMap>();
		HashMap<String, Usuario> usuarios = new HashMap<String, Usuario> ();
		HashMap<String, String> accesos = new HashMap<String, String> ();
		
		try (BufferedReader datos_usuarios = (new BufferedReader(new FileReader(base_datos.getFileUsuarios())))) {
			String linea = datos_usuarios.readLine();
			while (linea != null) {
				String[] columnas = linea.split(":");
				String nombre = columnas[0];
				int intentos = Integer.parseInt(columnas[1]);
				boolean bloqueado = false;
				if (columnas[2].equalsIgnoreCase("true")) {
					bloqueado = true;
				}			
				usuarios.put(nombre, new Usuario(nombre, intentos, bloqueado));
				linea = datos_usuarios.readLine();
			}
		} catch (FileNotFoundException e) {
			System.err.println("No se puede acceder al archivo \"historial de usuarios.txt\"");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
			e.printStackTrace();
		}
		try (BufferedReader datos_accesos = (new BufferedReader(new FileReader(base_datos.getFileAccesos())))) {
			String linea = datos_accesos.readLine();
			while (linea != null) {
				String[] columnas = linea.split(":");
				String nombre = columnas[0];
				String clave = columnas[1];
				accesos.put(nombre, clave);
				linea = datos_accesos.readLine();
			}
		} catch (FileNotFoundException e) {
			System.err.println("No se puede acceder al archivo \"accesos.txt\"");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
			e.printStackTrace();
		}
		
		bdd.put("accesos", accesos);
		bdd.put("usuarios", usuarios);
		return bdd;	
	}
	public static void actualizarFiles(BaseDatos base_datos, HashMap<String, HashMap> bdd) {

		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		HashMap<String, String> accesos = bdd.get("accesos");
		
		try (BufferedWriter datos_usuarios_temp = (new BufferedWriter(new FileWriter(base_datos.getFileUsuarios())))) {
			for (Entry<String, Usuario> entry : usuarios.entrySet()) {
				String k = entry.getKey();
				Usuario v = entry.getValue();
				String linea = k + ":" + v.getIntentos() + ":" + v.isBloqueado();
				datos_usuarios_temp.write(linea);
				datos_usuarios_temp.newLine();
			}
			base_datos.getFileUsuarios().delete();
			base_datos.getFile_usuarios_temp().renameTo(base_datos.getFileUsuarios());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
			e.printStackTrace();
		}
		
		try (BufferedWriter datos_accesos_temp = (new BufferedWriter(new FileWriter(base_datos.getFileAccesos())))) {
			for (Entry<String, String> entry : accesos.entrySet()) {
				String k = entry.getKey();
				String v = entry.getValue();
				String linea = k + ":" + v;
				datos_accesos_temp.write(linea);
				datos_accesos_temp.newLine();
			}
			base_datos.getFileAccesos().delete();
			base_datos.getFile_accesos_temp().renameTo(base_datos.getFileAccesos());
		} catch (IOException e) {
			System.err.println("Error de entrada/salida");
			e.printStackTrace();
		}
	}
	public static Usuario hacerLogin (HashMap<String, HashMap> bdd, BaseDatos base_datos) {
		Scanner input = new Scanner(System.in);
		Usuario user = null;
		String nombre = "";
		while (nombre.length() < 1) {
			System.out.println("Introduzca nombre:");
			nombre = input.nextLine();
		}
		boolean salir = false;
		int intentos = 3;
		do {
			System.out.println("Introduzca contraseña:");
			String pass = input.nextLine();
			if (existeNombreEnBdd(nombre, bdd) && estaBloqueado(nombre, bdd)) {
				System.err.println("El usuario \"" + nombre + "\" está bloqueado.");
				salir = true;
			} else if (existeNombreEnBdd(nombre, bdd) && claveCoincide(nombre, pass, bdd)) {
				user = new Usuario(nombre, 3, false);
				salir = true;
				Log.registrarLogAcceso(base_datos.getFileLog(), nombre, claveCoincide(nombre, pass, bdd));
			} else if (existeNombreEnBdd(nombre, bdd)) {
				if (!nombre.equals("admin")) {
					intentos--;
				}	
				Log.registrarLogAcceso(base_datos.getFileLog(), nombre, claveCoincide(nombre, pass, bdd));
				if (intentos == 0) {
					cambiarEstadoBloqueadoUsuario(nombre, bdd, true);
					// Posibilidad de registro nuevo, bloqueo, perdida de intentos. Se actualizan los ficheros.
					base_datos.actualizarFiles(base_datos, bdd);
					salir = true;
				} else if (intentos > 0) {
					System.err.println("La clave no coincide. Le quedan "+ intentos +" intentos.");
				}
			} else {
				System.err.println("No existe ese usuario.");
				Log.registrarLogAcceso(base_datos.getFileLog(), nombre, claveCoincide(nombre, pass, bdd));
				user = crearUsuario(base_datos, nombre, bdd);
				Log.registrarLogAcceso(base_datos.getFileLog(), nombre, claveCoincide(nombre, pass, bdd));
				salir = true;
			}
		} while (!salir);
		return user;
	}
	public static Usuario crearUsuario (BaseDatos base_datos, String nombre, HashMap<String, HashMap> bdd) {
		Usuario user = null;
		Scanner input = new Scanner(System.in);
		boolean exito = false;
		while (!exito) {
			System.out.println("Se procede a registrar al usuario " + nombre + ":");
			System.out.println("Introduzca contraseña:");
			String pass = input.nextLine();
			if (pass.length() > 0) {
				System.out.println("Confirme la contraseña:");
				String pass2 = input.nextLine();		
				if (pass.equals(pass2)) {
					exito = true;
					user = new Usuario(nombre, 3, false);
					bdd.get("usuarios").put(nombre, user);
					bdd.get("accesos").put(nombre, pass);
					// Posibilidad de registro nuevo, bloqueo, perdida de intentos. Se actualizan los ficheros.
					base_datos.actualizarFiles(base_datos, bdd);
				} else {
					System.err.println("No coinciden las contraseñas. Inténtelo de nuevo o pulse INTRO para volver atrás.");
				}
			} else {
				System.err.println("No se ha producido registro");
				exito = true;
			}
		}		
		return user;
	}
	private static boolean existeNombreEnBdd(String nombre, HashMap<String, HashMap> bdd) {
		boolean existe_nombre = bdd.get("usuarios").containsKey(nombre);
		return existe_nombre;
	}
	private static boolean claveCoincide(String nombre, String pass, HashMap<String, HashMap> bdd) {
		boolean coincide = false;
		if (pass.equals(bdd.get("accesos").get(nombre))) {
			coincide = true;
		}
		return coincide;
	}
	private static boolean estaBloqueado(String nombre, HashMap<String, HashMap> bdd) {
		boolean esta_bloqueado = false;
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		esta_bloqueado = usuarios.get(nombre).isBloqueado();
		return esta_bloqueado;
	}
	public static void bloqueoDesbloqueo(BaseDatos base_datos, HashMap<String, HashMap> bdd, boolean bloquear) {
		boolean dos = Pintar.pintarDosOpciones("¿Cuántos desea "+ (bloquear ? "" : "des") + "bloquear?", "Un usuario", "Todos los usuarios "+ (bloquear ? "des" : "") + "bloqueados");
		//boolean dos true es todos. false es uno
		if (!dos) {
			Scanner input = new Scanner(System.in);
			System.out.println("Cuál es el nombre de usuario que desea " + (bloquear ? "" : "des") + "bloquear:");
			String nombre = input.nextLine();
			if ((bloquear && existeNombreEnBdd(nombre, bdd) && estaBloqueado(nombre, bdd)) || (!bloquear && existeNombreEnBdd(nombre, bdd) && !estaBloqueado(nombre, bdd))) {
				System.err.println("El usuario \"" + nombre + "\" ya está " + (bloquear ? "" : "des") + "bloqueado.");
			} else if (bloquear && existeNombreEnBdd(nombre, bdd) && !estaBloqueado(nombre, bdd)) {
				base_datos.cambiarEstadoBloqueadoUsuario(nombre, bdd, bloquear);
			} else if (!bloquear && existeNombreEnBdd(nombre, bdd) && estaBloqueado(nombre, bdd)) {
				base_datos.cambiarEstadoBloqueadoUsuario(nombre, bdd, bloquear);
			} else if (!existeNombreEnBdd(nombre, bdd)) {
				System.err.println("El usuario \"" + nombre + "\" no existe.");
			}		
		} else {
			base_datos.cambiarEstadoBloqueadoTodosUsuarios(bdd, bloquear);
		}
		// Posibilidad de bloqueo o desbloqueo. Se actualizan los ficheros.
		base_datos.actualizarFiles(base_datos, bdd);
	}
	public static void cambiarEstadoBloqueadoTodosUsuarios(HashMap<String, HashMap> bdd, boolean bloquear) {
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		for (Entry<String, Usuario> entry : usuarios.entrySet()) {
			String nombre = entry.getKey();
			Usuario user = entry.getValue();
			if (bloquear && !user.isBloqueado()) {
				cambiarEstadoBloqueadoUsuario(nombre, bdd, bloquear);
			}
			if (!bloquear && user.isBloqueado()) {
				cambiarEstadoBloqueadoUsuario(nombre, bdd, bloquear);
			}
		}
	}
	public static void cambiarEstadoBloqueadoUsuario(String nombre, HashMap<String, HashMap> bdd, Boolean bloquear) {
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		if (!nombre.equals("admin") && bloquear) {
			usuarios.get(nombre).setBloqueado(bloquear ? true : false);
		}
		if (!nombre.equals("admin") && !bloquear) {
			usuarios.get(nombre).setBloqueado(!bloquear ? false : true);
		}
		if (nombre.equals("admin")) {
			System.err.println("El usuario \"" + nombre + "\" no se puede bloquear ni desbloquear.");
		} else {
			System.err.println("Usuario \"" + nombre + "\" " + (bloquear ? "" : "DES") + "BLOQUEADO");
		}
	}
}