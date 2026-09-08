package com.pg2.tetris;

public record HighScore(String playerName, int score) implements Comparable<HighScore> {
    @Override public int compareTo(HighScore other) { return Integer.compare(other.score, score); }
}
