package com.pg2.tetris;

public final class StandardTetromino extends Tetromino {
    public StandardTetromino(TetrominoType type, int row, int column) {
        super(type, row, column);
    }

    @Override
    public int[][] getCells() {
        return getType().cells(getRotation());
    }
}
