package com.pg2.tetris;

public final class GameBoard {
    private final int rows;
    private final int columns;
    private final TetrominoType[][] cells;

    public GameBoard(int columns, int rows) {
        this.rows = rows;
        this.columns = columns;
        this.cells = new TetrominoType[rows][columns];
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }
    public TetrominoType getCell(int row, int column) { return cells[row][column]; }

    public boolean canPlace(Tetromino piece, int row, int column, int rotation) {
        for (int[] cell : piece.getType().cells(rotation)) {
            int boardColumn = column + cell[0];
            int boardRow = row + cell[1];

            if (boardColumn < 0 || boardColumn >= columns || boardRow >= rows) {
                return false;
            }

            if (boardRow >= 0 && cells[boardRow][boardColumn] != null) {
                return false;
            }
        }
        return true;
    }

    public void lock(Tetromino piece) {
        for (int[] cell : piece.getCells()) {
            int column = piece.getColumn() + cell[0];
            int row = piece.getRow() + cell[1];
            if (row >= 0 && row < rows && column >= 0 && column < columns) {
                cells[row][column] = piece.getType();
            }
        }
    }

    public int clearFullRows() {
        int cleared = 0;

        for (int row = rows - 1; row >= 0; row--) {
            boolean full = true;
            for (TetrominoType cell : cells[row]) {
                if (cell == null) {
                    full = false;
                    break;
                }
            }

            if (full) {
                cleared++;
                for (int moveRow = row; moveRow > 0; moveRow--) {
                    System.arraycopy(cells[moveRow - 1], 0, cells[moveRow], 0, columns);
                }
                for (int column = 0; column < columns; column++) {
                    cells[0][column] = null;
                }
                row++; // re-check this row after rows above drop down
            }
        }
        return cleared;
    }
}
