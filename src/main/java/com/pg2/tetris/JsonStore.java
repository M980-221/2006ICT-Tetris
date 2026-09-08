package com.pg2.tetris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;

public final class JsonStore<T> {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;
    private final Type type;
    public JsonStore(Path path, Type type) { this.path = path; this.type = type; }
    public T load(T fallback) {
        if (!Files.exists(path)) return fallback;
        try (Reader reader = Files.newBufferedReader(path)) {
            T value = gson.fromJson(reader, type); return value == null ? fallback : value;
        } catch (IOException | RuntimeException e) { return fallback; }
    }
    public void save(T value) throws IOException {
        Path parent = path.getParent(); if (parent != null) Files.createDirectories(parent);
        Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
        try (Writer writer = Files.newBufferedWriter(temporary)) { gson.toJson(value, type, writer); }
        try { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
        catch (AtomicMoveNotSupportedException e) { Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING); }
    }
}
