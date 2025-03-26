package ec.edu.uees.model;

import java.util.LinkedList;

public class Vertex<E> {
    private E data;
    private LinkedList<Edge<E>> edges;
    private boolean visited;
    
    public Vertex(E data) {
        this.data = data;
        this.edges = new LinkedList<>();
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

    
}
