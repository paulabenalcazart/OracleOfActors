package ec.edu.uees.model;

import java.io.Serializable;
import java.util.LinkedList;

public class Vertex<E> implements Serializable {
    private E data;
    private LinkedList<Edge<E>> edges;
    private boolean visited;
    private Vertex<E> antecesor;
    private int distancia;

    public Vertex(E data) {
        this.data = data;
        this.edges = new LinkedList<>();
        this.distancia = Integer.MAX_VALUE;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public LinkedList<Edge<E>> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Edge<E>> edges) {
        this.edges = edges;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Vertex<E> getAntecesor() {
        return antecesor;
    }

    public void setAntecesor(Vertex<E> antecesor) {
        this.antecesor = antecesor;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
} 