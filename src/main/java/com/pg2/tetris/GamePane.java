package com.pg2.tetris;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public final class GamePane extends BorderPane implements GameEventListener {

    private static final double MAX_CELL_SIZE = 28;

    private final GameController controller;
    private final Canvas canvas;
    private final double cellSize;

    private final Label scoreLabel = new Label();

    private final Label helpLabel = new Label(
            "← → move   ↑ rotate   ↓ drop   Space hard drop   P pause"
    );

    private final Runnable onBack;

    private AnimationTimer timer;
    private long lastFrame;

    public GamePane(GameSettings settings, Runnable onBack) {

        this.onBack = onBack;

        this.controller =
                new GameController(
                        settings,
                        this
                );

        int columns =
                controller.getBoard()
                        .getColumns();

        int rows =
                controller.getBoard()
                        .getRows();

        /*
         * Scale the cells so every configured
         * board size fits inside the game window.
         */
        double availableWidth = 560;

        double availableHeight;

        if (rows <= 20) {
            availableHeight = 560;
        } else {
            availableHeight = 620;
        }

        this.cellSize =
                Math.min(
                        MAX_CELL_SIZE,
                        Math.min(
                                availableWidth / columns,
                                availableHeight / rows
                        )
                );

        this.canvas =
                new Canvas(
                        columns * cellSize,
                        rows * cellSize
                );

        buildLayout();
        configureKeyboard();
        updateScoreLabels();
        startLoop();
    }

    private void buildLayout() {

        Button backButton =
                new Button("Back");

        backButton.setOnAction(event -> {
            stop();
            onBack.run();
        });

        Label title =
                new Label("TETRIS");

        title.setFont(
                Font.font(26)
        );

        HBox header =
                new HBox(
                        20,
                        backButton,
                        title,
                        scoreLabel
                );

        header.setAlignment(
                Pos.CENTER
        );

        header.setPadding(
                new Insets(10)
        );

        /*
         * IMPORTANT:
         *
         * The StackPane is now exactly the same
         * size as the Canvas.
         *
         * Previously it expanded across the whole
         * center of the window. Because its
         * background was black, this caused the
         * large black areas on the left and right.
         */
        StackPane gameStack =
                new StackPane(canvas);

        gameStack.setPrefSize(
                canvas.getWidth(),
                canvas.getHeight()
        );

        gameStack.setMinSize(
                canvas.getWidth(),
                canvas.getHeight()
        );

        gameStack.setMaxSize(
                canvas.getWidth(),
                canvas.getHeight()
        );

        gameStack.setStyle(
                "-fx-background-color: #111111;" +
                        "-fx-border-color: #444444;"
        );

        gameStack.setFocusTraversable(
                true
        );

        /*
         * Centre the actual board in the window.
         */
        BorderPane.setAlignment(
                gameStack,
                Pos.CENTER
        );

        helpLabel.setPadding(
                new Insets(8)
        );

        helpLabel.setWrapText(
                true
        );

        helpLabel.setTextAlignment(
                TextAlignment.CENTER
        );

        helpLabel.setMaxWidth(
                Double.MAX_VALUE
        );

        helpLabel.setAlignment(
                Pos.CENTER
        );

        setTop(header);
        setCenter(gameStack);
        setBottom(helpLabel);

        setPadding(
                new Insets(10)
        );

        /*
         * The area outside the Tetris board
         * now uses the light application colour
         * instead of black.
         */
        setStyle(
                "-fx-background-color: #e8f4ff;"
        );

        setFocusTraversable(
                true
        );
    }

    private void configureKeyboard() {

        setOnKeyPressed(event -> {

            KeyCode key =
                    event.getCode();

            switch (key) {

                case LEFT ->
                        controller.moveLeft();

                case RIGHT ->
                        controller.moveRight();

                case UP ->
                        controller.rotateClockwise();

                case DOWN ->
                        controller.softDrop();

                case SPACE ->
                        controller.hardDrop();

                case P ->
                        controller.togglePause();

                default -> {
                    return;
                }
            }

            render();

            event.consume();
        });
    }

    private void startLoop() {

        timer =
                new AnimationTimer() {

                    @Override
                    public void handle(
                            long now
                    ) {

                        if (lastFrame == 0) {
                            lastFrame = now;
                        }

                        double elapsedSeconds =
                                (now - lastFrame)
                                        / 1_000_000_000.0;

                        lastFrame = now;

                        /*
                         * Prevent large animation jumps
                         * after window stalls.
                         */
                        controller.update(
                                Math.min(
                                        elapsedSeconds,
                                        0.05
                                )
                        );

                        render();
                    }
                };

        timer.start();

        requestFocus();
    }

    public void stop() {

        if (timer != null) {

            timer.stop();

            timer = null;
        }
    }

    private void render() {

        GraphicsContext gc =
                canvas.getGraphicsContext2D();

        GameBoard board =
                controller.getBoard();

        /*
         * Draw the Tetris board background.
         */
        gc.setFill(
                Color.rgb(
                        18,
                        18,
                        22
                )
        );

        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        /*
         * Draw pieces that are already
         * locked into the board.
         */
        for (
                int row = 0;
                row < board.getRows();
                row++
        ) {

            for (
                    int column = 0;
                    column < board.getColumns();
                    column++
            ) {

                TetrominoType type =
                        board.getCell(
                                row,
                                column
                        );

                if (type != null) {

                    drawCell(
                            gc,
                            column,
                            row,
                            type.getColor()
                    );
                }
            }
        }

        /*
         * Draw the currently falling piece.
         */
        Tetromino piece =
                controller.getCurrentPiece();

        if (
                piece != null
                        && !controller.isGameOver()
        ) {

            double visualRow =
                    piece.getRow()
                            + controller.getFallProgress();

            for (
                    int[] cell :
                    piece.getCells()
            ) {

                double column =
                        piece.getColumn()
                                + cell[0];

                double row =
                        visualRow
                                + cell[1];

                if (row > -1) {

                    drawCell(
                            gc,
                            column,
                            row,
                            piece.getType()
                                    .getColor()
                    );
                }
            }
        }

        drawGrid(
                gc,
                board
        );

        /*
         * Pause overlay.
         */
        if (
                controller.isPaused()
        ) {

            drawOverlay(
                    gc,
                    "PAUSED\nPress P to resume"
            );

        } else if (
                controller.isGameOver()
        ) {

            drawOverlay(
                    gc,
                    "GAME OVER\n"
                            + "Final score: "
                            + controller.getScore()
                            + "\nPress Back to return"
            );
        }
    }

    private void drawCell(
            GraphicsContext gc,
            double column,
            double row,
            Color color
    ) {

        double x =
                column * cellSize;

        double y =
                row * cellSize;

        double cornerRadius =
                Math.max(
                        2,
                        cellSize * 0.18
                );

        gc.setFill(
                color
        );

        gc.fillRoundRect(
                x + 1,
                y + 1,
                Math.max(
                        1,
                        cellSize - 2
                ),
                Math.max(
                        1,
                        cellSize - 2
                ),
                cornerRadius,
                cornerRadius
        );

        /*
         * Draw a lighter inner outline.
         */
        if (
                cellSize >= 6
        ) {

            gc.setStroke(
                    color.brighter()
            );

            gc.strokeRoundRect(
                    x + 2,
                    y + 2,
                    Math.max(
                            1,
                            cellSize - 4
                    ),
                    Math.max(
                            1,
                            cellSize - 4
                    ),
                    cornerRadius,
                    cornerRadius
            );
        }
    }

    private void drawGrid(
            GraphicsContext gc,
            GameBoard board
    ) {

        gc.setStroke(
                Color.rgb(
                        255,
                        255,
                        255,
                        0.10
                )
        );

        /*
         * Vertical grid lines.
         */
        for (
                int column = 0;
                column <= board.getColumns();
                column++
        ) {

            double x =
                    column * cellSize;

            gc.strokeLine(
                    x,
                    0,
                    x,
                    canvas.getHeight()
            );
        }

        /*
         * Horizontal grid lines.
         */
        for (
                int row = 0;
                row <= board.getRows();
                row++
        ) {

            double y =
                    row * cellSize;

            gc.strokeLine(
                    0,
                    y,
                    canvas.getWidth(),
                    y
            );
        }
    }

    private void drawOverlay(
            GraphicsContext gc,
            String text
    ) {

        gc.setFill(
                Color.rgb(
                        0,
                        0,
                        0,
                        0.72
                )
        );

        gc.fillRect(
                0,
                0,
                canvas.getWidth(),
                canvas.getHeight()
        );

        gc.setFill(
                Color.WHITE
        );

        double overlayFontSize =
                Math.min(
                        25,
                        Math.max(
                                16,
                                cellSize * 1.5
                        )
                );

        gc.setFont(
                Font.font(
                        overlayFontSize
                )
        );

        gc.setTextAlign(
                TextAlignment.CENTER
        );

        gc.fillText(
                text,
                canvas.getWidth() / 2,
                canvas.getHeight() / 2
        );

        gc.setTextAlign(
                TextAlignment.LEFT
        );
    }

    @Override
    public void onScoreChanged(
            int score,
            int lines
    ) {

        updateScoreLabels();
    }

    @Override
    public void onGameOver(
            int finalScore
    ) {

        updateScoreLabels();
    }

    private void updateScoreLabels() {

        scoreLabel.setText(
                "Score: "
                        + controller.getScore()
                        + "   Lines: "
                        + controller.getLines()
        );
    }
}