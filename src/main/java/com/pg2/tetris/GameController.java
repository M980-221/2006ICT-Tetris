package com.pg2.tetris;

public final class GameController {
    private final GameBoard board;
    private final GameEventListener listener;
    private final int startingLevel;
    private final PieceSequence sequence;
    private final TetrominoFactory factory;
    private int pieceIndex;

    private Tetromino currentPiece;
    private int score;
    private int lines;
    private boolean gameOver;
    private boolean paused;
    private double fallProgress;

    public GameController(GameSettings settings, GameEventListener listener) {
        this(settings, listener, new PieceSequence(), new TetrominoFactory());
    }

    public GameController(GameSettings settings, GameEventListener listener, PieceSequence sequence, TetrominoFactory factory) {
        this.board = new GameBoard(settings.getFieldWidth(), settings.getFieldHeight());
        this.startingLevel = settings.getLevel();
        this.listener = listener;
        this.sequence = sequence;
        this.factory = factory;
        spawnPiece();
    }

    public GameBoard getBoard() { return board; }
    public Tetromino getCurrentPiece() { return currentPiece; }
    public TetrominoType getNextPieceType() { return sequence.at(pieceIndex); }
    public int getScore() { return score; }
    public int getLines() { return lines; }
    public int getLevel() { return Math.min(10, startingLevel + lines / 10); }
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
        int effectiveLevel = getLevel();
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
        if (tryMove(0, -1)) AudioManager.getInstance().move();
    }

    public void moveRight() {
        if (tryMove(0, 1)) AudioManager.getInstance().move();
    }

    /**
     * Moves toward a server-selected column without ever blocking the UI.
     * Returns false when a wall or occupied cell prevents reaching the target.
     */
    public boolean moveToColumn(int requestedColumn) {
        if (paused || gameOver || currentPiece == null) return false;
        int target = Math.max(-2, Math.min(board.getColumns() - 1, requestedColumn));
        int remainingAttempts = board.getColumns() + 4;
        while (currentPiece.getColumn() != target && remainingAttempts-- > 0) {
            int direction = Integer.compare(target, currentPiece.getColumn());
            if (!tryMove(0, direction)) return false;
        }
        return currentPiece.getColumn() == target;
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
            AudioManager.getInstance().move();
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
        AudioManager.getInstance().hardDrop();
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
                AudioManager.getInstance().rotate();
                return;
            }
        }
    }

    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
            AudioManager.getInstance().pause();
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
        AudioManager.getInstance().lock();
        int cleared = board.clearFullRows();
        if (cleared > 0) {
            lines += cleared;
            score += ScoreRules.lineClearScore(cleared);
            AudioManager.getInstance().lineClear();
            notifyScore();
        }
        spawnPiece();
    }

    private void spawnPiece() {
        TetrominoType type = sequence.at(pieceIndex++);
        currentPiece = factory.create(type, board.getColumns());
        fallProgress = 0;

        if (!board.canPlace(currentPiece, currentPiece.getRow(), currentPiece.getColumn(), 0)) {
            gameOver = true;
            AudioManager.getInstance().gameOver();
            if (listener != null) listener.onGameOver(score);
        }
    }

    private void notifyScore() {
        if (listener != null) listener.onScoreChanged(score, lines);
    }
}
