package com.pg2.tetris;

public final class ScoreRules {
    private ScoreRules() {}
    public static int lineClearScore(int lines) {
        return switch (lines) { case 1 -> 100; case 2 -> 300; case 3 -> 600; case 4 -> 1000; default -> 0; };
    }
}
