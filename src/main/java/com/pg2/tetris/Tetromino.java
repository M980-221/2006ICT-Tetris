package com.pg2.tetris;

public abstract class Tetromino {
    private final TetrominoType type;
    private int row;
    private int column;
    private int rotation;

    protected Tetromino(TetrominoType type, int row, int column) {
        this.type = type;
        this.row = row;
        this.column = column;
    }

    public TetrominoType getType() { return type; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public int getRotation() { return rotation; }

    public void setRow(int row) { this.row = row; }
    public void setColumn(int column) { this.column = column; }
    public void setRotation(int rotation) { this.rotation = Math.floorMod(rotation, 4); }

    public abstract int[][] getCells();
}
