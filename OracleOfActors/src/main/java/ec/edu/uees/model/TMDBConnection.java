package ec.edu.uees.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TMDBConnection {
private final String apiKey;

    public TMDBConnection(String apiKey) {
        this.apiKey = apiKey;
    }

    public int buscarActorId(String nombre) throws Exception {
        String encoded = URLEncoder.encode(nombre, "UTF-8");
        String urlStr = "https://api.themoviedb.org/3/search/person?api_key=" + apiKey + "&query=" + encoded;
        JSONObject response = getJsonFromUrl(urlStr);
        JSONArray results = response.getJSONArray("results");
        if (results.length() == 0) return -1;
        return results.getJSONObject(0).getInt("id");
    }

    public List<Integer> obtenerPeliculasPorActor(int actorId) throws Exception {
        String urlStr = "https://api.themoviedb.org/3/person/" + actorId + "/movie_credits?api_key=" + apiKey;
        JSONObject response = getJsonFromUrl(urlStr);
        JSONArray cast = response.getJSONArray("cast");
        List<Integer> movieIds = new ArrayList<>();
        for (int i = 0; i < cast.length(); i++) {
            movieIds.add(cast.getJSONObject(i).getInt("id"));
        }
        return movieIds;
    }

    public List<Integer> obtenerActoresDePelicula(int movieId) throws Exception {
        String urlStr = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + apiKey;
        JSONObject response = getJsonFromUrl(urlStr);
        JSONArray cast = response.getJSONArray("cast");
        List<Integer> actorIds = new ArrayList<>();
        for (int i = 0; i < cast.length(); i++) {
            actorIds.add(cast.getJSONObject(i).getInt("id"));
        }
        return actorIds;
    }

    private JSONObject getJsonFromUrl(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return new JSONObject(response.toString());
    }    
}
