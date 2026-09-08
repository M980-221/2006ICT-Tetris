package com.pg2.tetris;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class HighScoreRepository {
    private final JsonStore<List<HighScore>> store;
    public HighScoreRepository() { this(Path.of("data", "highscores.json")); }
    public HighScoreRepository(Path path) { store = new JsonStore<>(path, new TypeToken<List<HighScore>>(){}.getType()); }
    public synchronized List<HighScore> findTopTen() {
        return store.load(new ArrayList<>()).stream().sorted().limit(10).toList();
    }
    public synchronized boolean qualifies(int score) { List<HighScore> scores = findTopTen(); return score > 0 && (scores.size() < 10 || score > scores.get(scores.size()-1).score()); }
    public synchronized void add(HighScore score) throws IOException {
        List<HighScore> scores = new ArrayList<>(store.load(new ArrayList<>())); scores.add(score);
        store.save(scores.stream().sorted().limit(10).toList());
    }
    public synchronized void clear() throws IOException { store.save(List.of()); }
}
