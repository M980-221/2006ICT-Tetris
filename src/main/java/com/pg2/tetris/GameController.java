package com.pg2.tetris;

import java.util.Random;

public final class GameController {
    private final GameBoard board;
    private final Random random = new Random();
    private final GameEventListener listener;
    private final int startingLevel;

    private Tetromino currentPiece;
    private int score;
    private int lines;
    private boolean gameOver;
    private boolean paused;
    private double fallProgress;

    public GameController(GameSettings settings, GameEventListener listener) {
        this.board = new GameBoard(settings.getFieldWidth(), settings.getFieldHeight());
        this.startingLevel = settings.getLevel();
        this.listener = listener;
        spawnPiece();
    }

    public GameBoard getBoard() { return board; }
    public Tetromino getCurrentPiece() { return currentPiece; }
    public int getScore() { return score; }
    public int getLines() { return lines; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public double getFallProgress() {
        if (currentPiece == null) {
            return 0;
        }

        // Only interpolate toward the next row when that row is actually valid.
        // This prevents a piece from being drawn one cell into the floor or into
        // an occupied stack immediately before it locks in place.
        boolean canMoveDown = board.canPlace(
                currentPiece,
                currentPiece.getRow() + 1,
                currentPiece.getColumn(),
                currentPiece.getRotation()
        );

        return canMoveDown ? fallProgress : 0;
    }

    public double getDropIntervalSeconds() {
        int effectiveLevel = Math.min(10, startingLevel + lines / 10);
        return Math.max(0.12, 0.75 - (effectiveLevel - 1) * 0.06);
    }

    public void update(double elapsedSeconds) {
        if (paused || gameOver || currentPiece == null) {
            return;
        }

        fallProgress += elapsedSeconds / getDropIntervalSeconds();
        while (fallProgress >= 1.0 && !gameOver) {
            fallProgress -= 1.0;
            if (!moveDown()) {
                fallProgress = 0;
                break;
            }
        }
    }

    public void moveLeft() {
        tryMove(0, -1);
    }

    public void moveRight() {
        tryMove(0, 1);
    }

    public boolean moveDown() {
        if (paused || gameOver) return false;
        if (tryMove(1, 0)) return true;
        lockCurrentPiece();
        return false;
    }

    public void softDrop() {
        if (moveDown()) {
            score += 1;
            notifyScore();
        }
        fallProgress = 0;
    }

    public void hardDrop() {
        if (paused || gameOver) return;
        int distance = 0;
        while (tryMove(1, 0)) {
            distance++;
        }
        score += distance * 2;
        lockCurrentPiece();
        fallProgress = 0;
        notifyScore();
    }

    public void rotateClockwise() {
        if (paused || gameOver || currentPiece == null) return;
        int nextRotation = (currentPiece.getRotation() + 1) % 4;

        // Simple wall kicks make rotation near walls more forgiving.
        int[] offsets = {0, -1, 1, -2, 2};
        for (int offset : offsets) {
            if (board.canPlace(currentPiece, currentPiece.getRow(), currentPiece.getColumn() + offset, nextRotation)) {
                currentPiece.setColumn(currentPiece.getColumn() + offset);
                currentPiece.setRotation(nextRotation);
                return;
            }
        }
    }

    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
        }
    }

    private boolean tryMove(int rowDelta, int columnDelta) {
        if (paused || gameOver || currentPiece == null) return false;
        int newRow = currentPiece.getRow() + rowDelta;
        int newColumn = currentPiece.getColumn() + columnDelta;

        if (board.canPlace(currentPiece, newRow, newColumn, currentPiece.getRotation())) {
            currentPiece.setRow(newRow);
            currentPiece.setColumn(newColumn);
            return true;
        }
        return false;
    }

    private void lockCurrentPiece() {
        board.lock(currentPiece);
        int cleared = board.clearFullRows();
        if (cleared > 0) {
            lines += cleared;
            score += switch (cleared) {
                case 1 -> 100;
                case 2 -> 300;
                case 3 -> 500;
                default -> 800;
            } * Math.max(1, startingLevel);
            notifyScore();
        }
        spawnPiece();
    }

    private void spawnPiece() {
        TetrominoType[] types = TetrominoType.values();
        TetrominoType type = types[random.nextInt(types.length)];
        currentPiece = new StandardTetromino(type, -1, Math.max(0, board.getColumns() / 2 - 2));
        fallProgress = 0;

        if (!board.canPlace(currentPiece, currentPiece.getRow(), currentPiece.getColumn(), 0)) {
            gameOver = true;
            if (listener != null) listener.onGameOver(score);
        }
    }

    private void notifyScore() {
        if (listener != null) listener.onScoreChanged(score, lines);
    }
}
