package dam2.add.p11;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Pintar {
	public static String pintarAreaAdmin(HashMap<String, HashMap> bdd) {
		String opcionAdmin = "";
			try {
				do {
					
					System.out.println("\n------ ÁREA ADMINISTRADOR ------");
					System.out.println(" [1] Mostrar Usuarios");
					System.out.println(" [2] Bloquear Usuarios");
					System.out.println(" [3] Desbloquear Usuarios");
					System.out.println(" [0] Volver");
					Scanner input = new Scanner(System.in);
					opcionAdmin = input.nextLine();
				} while (Integer.parseInt(opcionAdmin) <= 0 && Integer.parseInt(opcionAdmin) >=3);	
			} catch (NumberFormatException e) {
				System.err.println("Introduce una opción correcta");
			}
		
		return opcionAdmin;
	}
	public static void pintarMostrarUsuarios(HashMap<String, HashMap> bdd) {
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		System.out.println("- [ 1. MOSTRAR USUARIOS ] - ");
		pintarEstadosDeUsuarios(bdd);
	}
	public static void pintarEstadosDeUsuarios(HashMap<String, HashMap> bdd) {
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		for (Entry<String, Usuario> entry : usuarios.entrySet()) {
			String k = entry.getKey();
			Usuario v = entry.getValue();
			System.out.println("USUARIO: " + k + "\t INTENTOS: " + v.getIntentos() + "\t BLOQUEADO: " + v.isBloqueado());
		}
	}
	public static boolean pintarBloquearDesbloquearUsuarios(HashMap<String, HashMap> bdd, boolean dos) {
		boolean si_no = false;
		boolean desbloqueados = true;
		if (dos) {
			desbloqueados = false;
		}
		System.out.println("- [ " + (dos ? "2" : "3") + (dos ? ". " : ". DES") + "BLOQUEAR USUARIOS ] - ");
		boolean hay_lo_que_buscas = pintarUsuariosPorEstados(bdd, dos);
		if (hay_lo_que_buscas) {
			//true es sí. false es no
			si_no = pintarSiNo("¿Quiere " + (dos ? "" : "des") + "bloquear algún usuario?");
		}	
		return si_no;
	}
	public static boolean pintarUsuariosPorEstados(HashMap<String, HashMap> bdd, boolean dos) {
		boolean hay_lo_que_buscas = true;
		HashMap<String, Usuario> usuarios = bdd.get("usuarios");
		int contador = 0;
		for (Entry<String, Usuario> entry : usuarios.entrySet()) {
			String k = entry.getKey();
			Usuario v = entry.getValue();
			if (!v.isBloqueado() && dos) {
				System.out.println("USUARIO: " + k + "\t INTENTOS: " + v.getIntentos() + "\t BLOQUEADO: " + v.isBloqueado());
				contador++;
			}
			if (v.isBloqueado() && !dos) {
				System.out.println("USUARIO: " + k + "\t INTENTOS: " + v.getIntentos() + "\t BLOQUEADO: " + v.isBloqueado());
				contador++;
			}
		}
		if (contador == 0) {
			System.err.println("--- No hay usuarios " + (dos ? "des" : "") + "bloqueados ---");
			hay_lo_que_buscas = false;
		}
		return hay_lo_que_buscas;
	}
	public static boolean pintarSiNo(String pregunta) {
		boolean si_no = false;
		String respuesta = "";
		do {
			System.out.println("--- " + pregunta + " ---");	
			System.out.println(" [1] Sí\t[0] No");
			Scanner input = new Scanner(System.in);
			respuesta = input.nextLine();
		} while (!respuesta.equals("0") && !respuesta.equals("1"));
		if (respuesta.equals("1")) {
			si_no = true;
		}
		return si_no;
	}
	public static boolean pintarDosOpciones(String pregunta, String primera, String segunda) {
		boolean dos = true;
		String respuesta = "";
		do {
			System.out.println("--- " + pregunta + " ---");	
			System.out.println(" [1] " + primera+ "\t[2] " + segunda);
			Scanner input = new Scanner(System.in);
			respuesta = input.nextLine();
		} while (!respuesta.equals("1") && !respuesta.equals("2"));
		if (respuesta.equals("1")) {
			dos = false;
		}
		return dos;
	}
}
