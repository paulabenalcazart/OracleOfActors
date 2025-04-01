package ec.edu.uees.model;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class GraphLA<E> {
    private LinkedList<Vertex<E>> vertexes;
    private boolean directed;
    
    public GraphLA(boolean directed) {
        this.directed = directed;
        this.vertexes = new LinkedList<>();
    }

    public boolean addVertex(E data) {
        if(data == null || searchVertex(data) != null) return false;
        return vertexes.add(new Vertex<>(data));
    }

    public boolean addEdge(E src, E dst, int peso) {
        if(src == null || dst == null) return false;
        Vertex<E> vo = searchVertex(src);
        Vertex<E> vd = searchVertex(dst);
        if(vo == null || vd == null) return false;
        Edge<E> e = new Edge<>(vo, vd, peso);
        if (!vo.getEdges().contains(e)) {
            vo.getEdges().add(e); 
            if(!directed) {
                Edge<E> e1 = new Edge<>(vd, vo, peso);
                vd.getEdges().add(e1);
            }
            return true;
        }
        return false;        
    }

    private Vertex<E> searchVertex(E data) {
        for(Vertex<E> v: vertexes) {
            if(v.getData().equals(data)) return v;
        }
        return null;
    }

    @Override
    public String toString() {
        if(vertexes.isEmpty()) return "V={}\nA={}\n";
        StringBuilder sb = new StringBuilder();
        sb.append("V={");
        int i = 0;
        for(Vertex<E> v : vertexes) {
            sb.append(v.getData());
            if(i != vertexes.size()-1) {
                sb.append(",");
            }
            i++;
        }
        sb.append("}\nA={");
        i = 0;
        for(Vertex<E> v : vertexes) {
            for(Edge<E> e: v.getEdges()) {
                sb.append("(").append(e.getSrc().getData()).append(",").append(e.getDts().getData()).append(",").append(e.getPeso()).append(")");
            }
            if(i != vertexes.size()-1) {
                sb.append(",");
            }
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
    
    public List<E> caminoMinimo(E src, E dst) {
        List<E> lista = new LinkedList<>();
        Vertex<E> v = searchVertex(src);
        dijkstra(v);
        Vertex<E> d = searchVertex(dst);
        lista.add(d.getData());
        while(d.getAntecesor() != null) {
            lista.add(d.getAntecesor().getData());
            d = d.getAntecesor();
        }
        return lista.reversed();
    }

    private void dijkstra(Vertex<E> v) {
        PriorityQueue<Vertex<E>> cola = new PriorityQueue<>((Vertex<E> v1, Vertex<E> v2) -> Integer.compare(v1.getDistancia(),v2.getDistancia()));
        v.setDistancia(0);
        cola.offer(v);
        while(!cola.isEmpty()) {
            v = cola.poll();
            v.setVisited(true);
            for(Edge<E> e : v.getEdges()) {
                if(!e.getDts().isVisited()) {
                    if(v.getDistancia() + e.getPeso() < e.getDts().getDistancia()) {
                        e.getDts().setAntecesor(v);
                        e.getDts().setDistancia(v.getDistancia() + e.getPeso());
                        cola.offer(e.getDts());
                    }
                }
            }
        }
    }
}