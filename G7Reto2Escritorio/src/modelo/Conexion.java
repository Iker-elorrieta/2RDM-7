package modelo;

import java.io.IOException;
import java.net.Socket;

public class Conexion {
	public static String direccion = "10.5.13.33";
	public static int puerto = 3000;
	
	public static Socket conexion;
	
	public Conexion() throws IOException {
		Conexion.conexion = new Socket(direccion, puerto);
	}
}
