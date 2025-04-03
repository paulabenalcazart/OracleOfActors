package ec.edu.uees.controller;

import ec.edu.uees.model.GraphLA;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GraphLoader {
    private final String grafoPath;

    public GraphLoader() {
        this.grafoPath = "grafo.ser";
    }

    @SuppressWarnings("unchecked")
    public GraphLA<String> cargarGrafo() {
        File archivo = new File(grafoPath);
        if (!archivo.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (GraphLA<String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No se pudo cargar el grafo: " + e.getMessage());
            return null;
        }
    }

    public boolean guardarGrafo(GraphLA<String> grafo) {
        try {
            File archivo = new File(grafoPath);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(grafo);
            }
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo guardar el grafo: " + e.getMessage());
            return false;
        }
    }
}