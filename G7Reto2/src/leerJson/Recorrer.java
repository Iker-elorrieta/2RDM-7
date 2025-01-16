package leerJson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Recorrer {

	public static void main(String[] args) {

		final String url = "json/Centros-Lat-Lon.json";

		JsonParser parser = new JsonParser();

		try {
			FileReader fr = new FileReader(url);
			JsonElement datos = parser.parse(fr);
			JsonObject objetoRaiz = datos.getAsJsonObject();
			JsonArray array = objetoRaiz.getAsJsonArray("CENTROS");

			for (JsonElement elemento : array) {
				JsonObject objeto = elemento.getAsJsonObject();

				System.out.println("Centro:");
				for (Map.Entry<String, JsonElement> entry : objeto.entrySet()) {
					System.out.println("\t" + entry.getKey() + ": " + entry.getValue().getAsString());
				}
				System.out.println();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
