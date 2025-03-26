package ec.edu.uees.model;

public class Edge<E> {
    private Vertex<E> src;
    private Vertex<E> dts;
    private int peso;
    
    public Edge(Vertex<E> src, Vertex<E> dts, int peso) {
        this.src = src;
        this.dts = dts;
        this.peso = peso;
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Edge other = (Edge) obj;
        return (this.src.getData().equals(other.src.getData()) && this.dts.getData().equals(other.dts.getData()));
    }
}