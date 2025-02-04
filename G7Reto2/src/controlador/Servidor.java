package controlador;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import modelo.Centro;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Servidor {

	private static final String url = "json/Centros-Lat-Lon.json";
	
	public static void main(String[] args) {
		int id = 1; 
		ServerSocket serverSocket = null;
		
		HashMap<Integer, String> centros = new HashMap<Integer, String>();

		JsonParser parser = new JsonParser();
		try {
			FileReader fr = new FileReader(url);
			JsonElement datos = parser.parse(fr);
			JsonObject objetoRaiz = datos.getAsJsonObject();
			JsonArray array = objetoRaiz.getAsJsonArray("CENTROS");

			for (JsonElement elemento : array) {
				JsonObject objeto = elemento.getAsJsonObject();
				
				centros.put(objeto.get("CCEN").getAsInt(), objeto.get("NOM").getAsString());
				
				/*System.out.println("Centro:");
				for (Map.Entry<String, JsonElement> entry : objeto.entrySet()) {
					
					System.out.println("\t" + entry.getKey() + ": " + entry.getValue().getAsString());
				}
				System.out.println();*/
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			serverSocket = new ServerSocket(3000);
			System.out.println("Servidor iniciado...");

			while (true) {
				Socket cliente = serverSocket.accept();
				System.out.println("=>Cliente conectado: " + id);
				System.out.println("Dirección IP: " + cliente.getInetAddress());

				HiloServidor clienteThread = new HiloServidor(cliente, id, centros);
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
