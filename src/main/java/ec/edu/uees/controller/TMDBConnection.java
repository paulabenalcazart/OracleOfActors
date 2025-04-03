package ec.edu.uees.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class TMDBConnection {
    private final String apiKey;

    public TMDBConnection(String apiKey) {
        this.apiKey = apiKey;
    }

    public String buscarActorId(String nombre) {
        try {
            String encoded = URLEncoder.encode(nombre, StandardCharsets.UTF_8);
            String url = "https://api.themoviedb.org/3/search/person?api_key=" + apiKey + "&query=" + encoded;
            JSONObject json = new JSONObject(getJson(url));
            JSONArray results = json.getJSONArray("results");
            if (results.length() > 0) {
                int id = results.getJSONObject(0).getInt("id");
                System.out.println("ID encontrado para " + nombre + ": " + id);
                return String.valueOf(id);
            }
        } catch (Exception e) {
            System.err.println("Error buscando ID del actor: " + e.getMessage());
        }
        return null;
    }

    public List<String> obtenerPeliculas(String actorId) {
    List<String> peliculas = new ArrayList<>();
    try {
        String urlStr = "https://api.themoviedb.org/3/person/" + actorId + "/movie_credits?api_key=" + apiKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray castArray = json.getJSONArray("cast");

        System.out.println("Películas encontradas para actor " + actorId + ": " + castArray.length());

        for (int i = 0; i < castArray.length(); i++) {
            JSONObject movie = castArray.getJSONObject(i);
            String id = String.valueOf(movie.getInt("id"));
            String titulo = movie.optString("title", "Sin título");
            peliculas.add(id);
            System.out.println("→ " + titulo + " (ID: " + id + ")");
        }

    } catch (Exception e) {
        System.err.println("Error obteniendo películas: " + e.getMessage());
    }
    return peliculas;
}


    public List<String> obtenerCoactores(String movieId) {
        List<String> coactores = new ArrayList<>();
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + apiKey;
            JSONObject json = new JSONObject(getJson(url));
            JSONArray cast = json.getJSONArray("cast");
            for (int i = 0; i < cast.length(); i++) {
                coactores.add(cast.getJSONObject(i).getString("name"));
            }
            System.out.println("Coactores en película " + movieId + ": " + coactores.size());
        } catch (Exception e) {
            System.err.println("Error obteniendo coactores: " + e.getMessage());
        }
        return coactores;
    }

    public String obtenerNombrePelicula(String movieId) {
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
            JSONObject json = new JSONObject(getJson(url));
            return json.getString("title");
        } catch (Exception e) {
            System.err.println("Error obteniendo nombre de película: " + e.getMessage());
            return "Película desconocida";
        }
    }

    private String getJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}