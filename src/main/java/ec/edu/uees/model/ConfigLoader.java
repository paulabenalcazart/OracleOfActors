package ec.edu.uees.model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ConfigLoader {
    public static String getTmdbApiKey() {
        try (InputStream is = ConfigLoader.class.getResourceAsStream("/connection/config.json")) {
            if (is == null) {
                System.err.println("No se encontró el archivo config.json");
                return null;
            }

            String jsonText = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));

            JSONObject json = new JSONObject(jsonText);
            return json.getString("tmdb_api_key");

        } catch (Exception e) {
            System.err.println("Error al leer config.json: " + e.getMessage());
            return null;
        }
    }
}