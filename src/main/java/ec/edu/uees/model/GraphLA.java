package ec.edu.uees.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class GraphLA<E> implements Serializable {
    private LinkedList<Vertex<E>> vertexes;
    private boolean directed;

    public GraphLA(boolean directed) {
        this.directed = directed;
        this.vertexes = new LinkedList<>();
    }

    public boolean addVertex(E data) {
        if (data == null || searchVertex(data) != null) return false;
        return vertexes.add(new Vertex<>(data));
    }

    public boolean addEdge(E src, E dst, int peso) {
        return addEdge(src, dst, peso, null);
    }

    public boolean addEdge(E src, E dst, int peso, String pelicula) {
        if (src == null || dst == null) return false;
        Vertex<E> vo = searchVertex(src);
        Vertex<E> vd = searchVertex(dst);
        if (vo == null || vd == null) return false;

        Edge<E> e = new Edge<>(vo, vd, peso, pelicula);
        if (!vo.getEdges().contains(e)) {
            vo.getEdges().add(e);
            if (!directed) {
                Edge<E> e1 = new Edge<>(vd, vo, peso, pelicula);
                vd.getEdges().add(e1);
            }
            return true;
        }
        return false;
    }

    public String getPeliculaEntre(E a, E b) {
        Vertex<E> v1 = searchVertex(a);
        Vertex<E> v2 = searchVertex(b);
        if (v1 == null || v2 == null) return null;
        for (Edge<E> e : v1.getEdges()) {
            if (e.getDts().equals(v2)) return e.getPelicula();
        }
        if (!directed) {
            for (Edge<E> e : v2.getEdges()) {
                if (e.getDts().equals(v1)) return e.getPelicula();
            }
        }
        return null;
    }
    
    private Vertex<E> searchVertex(E data) {
        for (Vertex<E> v : vertexes) {
            if (v.getData().equals(data)) return v;
        }
        return null;
    }

    public List<E> caminoMinimo(E src, E dst) {
        for (Vertex<E> v : vertexes) {
            v.setDistancia(Integer.MAX_VALUE);
            v.setAntecesor(null);
            v.setVisited(false);
        }

        List<E> lista = new LinkedList<>();
        Vertex<E> v = searchVertex(src);
        dijkstra(v);
        Vertex<E> d = searchVertex(dst);
        if (d == null || d.getAntecesor() == null) return lista;
        while (d != null) {
            lista.add(0, d.getData());
            d = d.getAntecesor();
        }
        return lista;
    }

    private void dijkstra(Vertex<E> v) {
        PriorityQueue<Vertex<E>> cola = new PriorityQueue<>(Comparator.comparingInt(Vertex::getDistancia));
        v.setDistancia(0);
        cola.offer(v);
        while (!cola.isEmpty()) {
            v = cola.poll();
            v.setVisited(true);
            for (Edge<E> e : v.getEdges()) {
                if (!e.getDts().isVisited()) {
                    if (v.getDistancia() + e.getPeso() < e.getDts().getDistancia()) {
                        e.getDts().setAntecesor(v);
                        e.getDts().setDistancia(v.getDistancia() + e.getPeso());
                        cola.offer(e.getDts());
                    }
                }
            }
        }
    }

    public LinkedList<Vertex<E>> getVertexes() {
        return vertexes;
    }
    
    public boolean contieneVertice(E data) {
        return searchVertex(data) != null;
    }

} 