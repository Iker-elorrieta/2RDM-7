package controlador;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Servidor {

	public static void main(String[] args) {
		int id = 1; 
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(3000);
			System.out.println("Servidor iniciado...");

			while (true) {
				Socket cliente = serverSocket.accept();
				System.out.println("=>Cliente conectado: " + id);
				System.out.println("Dirección IP: " + cliente.getInetAddress());

				HiloServidor clienteThread = new HiloServidor(cliente, id);
				clienteThread.start();

				id++;
			}

		} catch (IOException e) {
			System.out.println("Error en el servidor: " + e.getMessage());
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
					System.out.println("Servidor cerrado.");
				} catch (IOException e) {
					System.out.println("Error al cerrar el servidor: " + e.getMessage());
				}
			}
		}
	}
}
