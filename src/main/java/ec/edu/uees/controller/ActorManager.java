package ec.edu.uees.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

public class ActorManager {
    private final String actorFilePath;

    public ActorManager() {
        this.actorFilePath = "actors.txt";
    }

    public Set<String> leerActores() {
        Set<String> actores = new LinkedHashSet<>();
        Path path = Paths.get(actorFilePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            String contenido = new String(Files.readAllBytes(path));
            String[] partes = contenido.split(",");
            for (String actor : partes) {
                String limpio = actor.trim();
                if (!limpio.isEmpty()) actores.add(limpio);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer o crear el archivo de actores: " + e.getMessage());
        }
        return actores;
    }

    public boolean agregarActor(String nuevoActor) {
        Set<String> actores = leerActores();
        if (actores.contains(nuevoActor)) return false;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(actorFilePath, true))) {
            if (!actores.isEmpty()) writer.write(",");
            writer.write(nuevoActor);
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo escribir el actor en el archivo: " + e.getMessage());
            return false;
        }
    }

    public boolean contieneActor(String actor) {
        return leerActores().contains(actor);
    }
}