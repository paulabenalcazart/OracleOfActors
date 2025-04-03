package ec.edu.uees.model;

import java.io.Serializable;

public class Edge<E> implements Serializable {
    private Vertex<E> src;
    private Vertex<E> dts;
    private int peso;
    private String pelicula;

    public Edge(Vertex<E> src, Vertex<E> dts, int peso) {
        this.src = src;
        this.dts = dts;
        this.peso = peso;
    }

    public Edge(Vertex<E> src, Vertex<E> dts, int peso, String pelicula) {
        this.src = src;
        this.dts = dts;
        this.peso = peso;
        this.pelicula = pelicula;
    }

    public Vertex<E> getSrc() {
        return src;
    }

    public void setSrc(Vertex<E> src) {
        this.src = src;
    }

    public Vertex<E> getDts() {
        return dts;
    }

    public void setDts(Vertex<E> dts) {
        this.dts = dts;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getPelicula() {
        return pelicula;
    }

    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge<?> other = (Edge<?>) obj;
        return src.getData().equals(other.src.getData()) && dts.getData().equals(other.dts.getData());
    }
}