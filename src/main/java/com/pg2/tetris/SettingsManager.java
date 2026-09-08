package com.pg2.tetris;

import java.io.IOException;
import java.nio.file.Path;

/** Lazy-holder Singleton: JVM class initialization makes construction thread-safe. */
public final class SettingsManager {
    private final JsonStore<GameSettings> store = new JsonStore<>(Path.of("data", "settings.json"), GameSettings.class);
    private GameSettings settings;
    private SettingsManager() { settings = store.load(new GameSettings()); }
    private static class Holder { private static final SettingsManager INSTANCE = new SettingsManager(); }
    public static SettingsManager getInstance() { return Holder.INSTANCE; }
    public synchronized GameSettings get() { return settings; }
    public synchronized void save() { try { store.save(settings); } catch (IOException e) { System.err.println("Settings save failed: " + e.getMessage()); } }
}
