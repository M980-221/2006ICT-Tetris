package com.pg2.tetris;

public final class TetrominoFactory {
    public Tetromino create(TetrominoType type, int boardWidth) {
        return new StandardTetromino(type, -1, Math.max(0, boardWidth / 2 - 2));
    }
}
