package ec.edu.uees.controller;

import ec.edu.uees.model.GraphLA;
import ec.edu.uees.model.Vertex;
import java.util.ArrayList;
import java.util.List;

public class GraphExpander {
    private final TMDBConnection tmdb;
    private final ActorManager actorManager;
    private final GraphLoader graphLoader;

    private String ultimoActorAgregado = null;

    public GraphExpander(TMDBConnection tmdb, ActorManager actorManager, GraphLoader graphLoader) {
        this.tmdb = tmdb;
        this.actorManager = actorManager;
        this.graphLoader = graphLoader;
    }
    
    private String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String[] partes = nombre.trim().toLowerCase().split(" ");
        StringBuilder capitalizado = new StringBuilder();
        for (String parte : partes) {
            if (!parte.isEmpty()) {
                capitalizado.append(Character.toUpperCase(parte.charAt(0)))
                            .append(parte.substring(1))
                            .append(" ");
            }
        }
        return capitalizado.toString().trim();
    }
    
    public boolean agregarActorAlGrafo(GraphLA<String> grafo, String actorNombre) throws InterruptedException {
        if (grafo == null || actorNombre == null || actorNombre.isBlank()) return false;
        actorNombre = capitalizarNombre(actorNombre);
        System.out.println("Buscando ID para: " + actorNombre);
        String actorId = tmdb.buscarActorId(actorNombre);
        if (actorId == null) return false;

        System.out.println("Obteniendo películas de: " + actorNombre);
        List<String> peliculas = tmdb.obtenerPeliculas(actorId);
        if (peliculas == null || peliculas.isEmpty()) return false;

        String mejorPeliculaId = null;
        List<String> mejoresCoactores = new ArrayList<>();
        int maxCoincidencias = 0;

        // Buscar la película con más coactores ya en el grafo
        for (int i = 0; i < Math.min(10, peliculas.size()); i++) {
            String peliculaId = peliculas.get(i);
            List<String> coactores = tmdb.obtenerCoactores(peliculaId);
            int coincidencias = 0;
            for (String coactor : coactores) {
                if (grafo.getVertexes().stream().anyMatch(v -> v.getData().equalsIgnoreCase(coactor))) {
                    coincidencias++;
                }
            }
            if (coincidencias > maxCoincidencias) {
                maxCoincidencias = coincidencias;
                mejorPeliculaId = peliculaId;
                mejoresCoactores = coactores;
            }
        }

        // Si no se encontró conexión, usa la primera película
        if (mejorPeliculaId == null || mejoresCoactores.isEmpty()) {
            System.out.println("No se encontraron coactores conectables, usando primera película para inicializar red...");
            mejorPeliculaId = peliculas.get(0);
            mejoresCoactores = tmdb.obtenerCoactores(mejorPeliculaId);
        }

        String nombrePelicula = tmdb.obtenerNombrePelicula(mejorPeliculaId);
        boolean conectado = false;

        // Agregar el actor solo si no está
        if (!grafo.contieneVertice(actorNombre)) {
            grafo.addVertex(actorNombre);
        }

        // Agregar coactores con límite y delay
        int coactorMax = 10;
        int agregados = 0;

        for (String coactor : mejoresCoactores) {
            if (!coactor.equalsIgnoreCase(actorNombre) && agregados < coactorMax) {
                if (!grafo.contieneVertice(coactor)) {
                    grafo.addVertex(coactor);
                    actorManager.agregarActor(coactor);
                    Thread.sleep(200); // Pausa para evitar saturar API
                }

                grafo.addEdge(actorNombre, coactor, 1, nombrePelicula);
                conectado = true;
                agregados++;
            }
        }

        actorManager.agregarActor(actorNombre);
        graphLoader.guardarGrafo(grafo);

        // Verificar conexión cruzada con actores ya en el grafo
        for (Vertex<String> v : grafo.getVertexes()) {
            if (!v.getData().equals(actorNombre) && grafo.getPeliculaEntre(actorNombre, v.getData()) == null) {
                String idB = tmdb.buscarActorId(v.getData());
                if (idB != null) {
                    List<String> pelisB = tmdb.obtenerPeliculas(idB);
                    for (String pa : peliculas) {
                        if (pelisB.contains(pa)) {
                            String nombrePeli = tmdb.obtenerNombrePelicula(pa);
                            grafo.addEdge(actorNombre, v.getData(), 1, nombrePeli);
                            System.out.println("Conexión forzada: " + actorNombre + " ↔ " + v.getData() + " por " + nombrePeli);
                            graphLoader.guardarGrafo(grafo);
                            return true;
                        }
                    }
                }
            }
        }

        // Conexión con el último actor agregado
        if (ultimoActorAgregado != null && !ultimoActorAgregado.equals(actorNombre)
                && grafo.getPeliculaEntre(actorNombre, ultimoActorAgregado) == null) {
            String idUltimo = tmdb.buscarActorId(ultimoActorAgregado);
            if (idUltimo != null) {
                List<String> pelisUltimo = tmdb.obtenerPeliculas(idUltimo);
                for (String pa : peliculas) {
                    if (pelisUltimo.contains(pa)) {
                        String nombrePeli = tmdb.obtenerNombrePelicula(pa);
                        grafo.addEdge(actorNombre, ultimoActorAgregado, 1, nombrePeli);
                        System.out.println("Conexión directa entre actores recién agregados: " + actorNombre + " ↔ " + ultimoActorAgregado + " por " + nombrePeli);
                        graphLoader.guardarGrafo(grafo);
                        break;
                    }
                }
            }
        }

        ultimoActorAgregado = actorNombre;
        return true;
    }
}