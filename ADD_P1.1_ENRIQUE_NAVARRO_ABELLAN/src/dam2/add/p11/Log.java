package dam2.add.p11;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

	public static boolean registrarLogAcceso (File ruta_log, String nombre, boolean resultado) {
		boolean exito = false;
		
		try (BufferedWriter log_write = new BufferedWriter(new FileWriter(ruta_log, true))) {
			log_write.write(new Date() + "  ----  Intento de Inicio de Sesión " + (resultado? "" : "IN") + "CORRECTO  ----  " + nombre);
			log_write.newLine();
		} catch (IOException e) {
			System.out.println("Error de entrada/salida");
			e.printStackTrace();
		}
		return exito;
	}
//	public static boolean registrarLogAcceso (File ruta_log, Usuario usuario, boolean resultado) {
//		boolean exito = false;
//		BufferedWriter log_write = null;
//		try {
//			log_write = new BufferedWriter(new FileWriter(ruta_log, true));
//			log_write.write(new Date() + "  ----  Intento de registro " + (resultado? "" : "IN") + "CORRECTO  ----  " + usuario.getNombre());
//			log_write.newLine();
//
//		} catch (IOException e) {
//			System.out.println("Error de entrada/salida");
//			e.printStackTrace();
//		} finally {
//			try {
//				log_write.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}		
//		return exito;
//	}
}
