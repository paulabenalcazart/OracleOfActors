package ec.edu.uees.model;

import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
    public static String getTmdbApiKey(){
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/connection/config.json")));
            JSONObject json = new JSONObject(content);
            return json.getString("tmdb_api_key");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
