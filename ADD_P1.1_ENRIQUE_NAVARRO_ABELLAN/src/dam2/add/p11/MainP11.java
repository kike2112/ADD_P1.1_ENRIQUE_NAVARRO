package dam2.add.p11;

import java.io.File;
import java.util.HashMap;

public class MainP11 {

	public static void main(String[] args) {
		
		File fileUsuarios = new File("src" + File.separator + "dam2" + File.separator + "add" + File.separator + "p11" + File.separator + "historial_usuarios.txt");
		File fileAcesos = new File("src" + File.separator + "dam2" + File.separator + "add" + File.separator + "p11" + File.separator + "acceso.txt");
		File file_usuarios_temp = new File("src" + File.separator + "dam2" + File.separator + "add" + File.separator + "p11" + File.separator + "actualizacion_usuarios_temp.txt");
		File file_accesos_temp = new File("src" + File.separator + "dam2" + File.separator + "add" + File.separator + "p11" + File.separator + "actualizacion_accesos_temp.txt");
		File fileLog = new File("src" + File.separator + "dam2" + File.separator + "add" + File.separator + "p11" + File.separator + "loging.log");
		// BaseDatos base_datos --- se utilizará para manipular los ficheros persistentes.
		BaseDatos base_datos = new BaseDatos(fileUsuarios, fileAcesos, file_usuarios_temp, file_accesos_temp, fileLog);
		// HashMap<String, HashMap> bdd --- será una copia de la base de datos en el IDE. Se actualiza con los datos de los ficheros o bien actualiza los ficheros con sus datos nuevos. 
		HashMap<String, HashMap> bdd= new HashMap<String, HashMap>();
		bdd = base_datos.recargarUsuarios(base_datos);
		
		Usuario usuario_actual = null;
		boolean salir = false;
		while (!salir) {
			while (usuario_actual == null) {
				usuario_actual = base_datos.hacerLogin(bdd, base_datos);
				if (usuario_actual != null) {
					System.out.println("Hola " + usuario_actual.getNombre());
					salir = true;
				} else {
					System.out.println("Vuelve a intentarlo");
				}
			}
			if (usuario_actual.getNombre().equals("admin")) {
				boolean volver = false;
				while (!volver) {
					String opcionAdmin = Pintar.pintarAreaAdmin(bdd);
					if (opcionAdmin.equalsIgnoreCase("1")) {
						Pintar.pintarMostrarUsuarios(bdd);
					} else if (opcionAdmin.equalsIgnoreCase("2") || opcionAdmin.equalsIgnoreCase("3")) {
						boolean dos = true;
						if (opcionAdmin.equalsIgnoreCase("3")) {
							dos = false;
						}
						boolean bloquear = Pintar.pintarBloquearDesbloquearUsuarios(bdd, dos);
						if (bloquear) {
							base_datos.bloqueoDesbloqueo(base_datos, bdd, dos);
						}
					} else if (opcionAdmin.equalsIgnoreCase("0")) {
						volver = true;
					}
				}
				salir = true;
			}
		}
		System.out.println("Programa CERRADO");
	}
}
