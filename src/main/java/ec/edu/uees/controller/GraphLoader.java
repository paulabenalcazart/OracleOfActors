package ec.edu.uees.controller;

import ec.edu.uees.model.GraphLA;
import java.io.*;

public class GraphLoader {

    // Ruta segura y persistente para el archivo serializado
    private final String grafoPath = System.getProperty("user.home") + File.separator + ".oracleofactors" + File.separator + "grafo.ser";

    public GraphLoader() {
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(System.getProperty("user.home") + File.separator + ".oracleofactors");
        if (!dir.exists()) {
            dir.mkdirs(); // Crea la carpeta si no existe
        }
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
            if (!archivo.exists()) archivo.createNewFile();

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
