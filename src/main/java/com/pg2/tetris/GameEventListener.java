package com.pg2.tetris;

public interface GameEventListener {
    void onScoreChanged(int score, int lines);
    void onGameOver(int finalScore);
}
