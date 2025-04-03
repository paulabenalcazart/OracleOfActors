package ec.edu.uees.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class ActorManager {

    private final String actorFilePath = System.getProperty("user.home") + File.separator + ".oracleofactors" + File.separator + "actors.txt";

    public ActorManager() {
        ensureFileExists();
    }

    private String capitalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String[] partes = nombre.trim().toLowerCase(Locale.ROOT).split(" ");
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

    private void ensureFileExists() {
        try {
            Path path = Path.of(actorFilePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.err.println("No se pudo crear el archivo de actores: " + e.getMessage());
        }
    }

    public Set<String> leerActores() {
        Set<String> actores = new LinkedHashSet<>();
        try {
            Path path = Path.of(actorFilePath);
            String contenido = Files.readString(path);
            String[] partes = contenido.split(",");
            for (String actor : partes) {
                String limpio = capitalizarNombre(actor);
                if (!limpio.isEmpty()) actores.add(limpio);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer el archivo de actores: " + e.getMessage());
        }
        return actores;
    }

    public boolean agregarActor(String nuevoActor) {
        String limpio = capitalizarNombre(nuevoActor);
        Set<String> actores = leerActores();

        if (actores.contains(limpio)) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(actorFilePath, true))) {
            if (!actores.isEmpty()) writer.write(",");
            writer.write(limpio);
            return true;
        } catch (IOException e) {
            System.err.println("No se pudo escribir el actor en el archivo: " + e.getMessage());
            return false;
        }
    }

    public boolean contieneActor(String actor) {
        String limpio = capitalizarNombre(actor);
        return leerActores().contains(limpio);
    }
}
