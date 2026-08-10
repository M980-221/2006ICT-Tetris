package com.pg2.tetris;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class TetrisApplication extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("2006ICT Tetris");
        stage.setResizable(false);
        showSplashScreen();
        stage.show();
    }

    private void showSplashScreen() {
        Label title = new Label("PG2 TETRIS");
        title.setFont(Font.font(38));

        Label course = new Label(
                "2006ICT Object Oriented Software Development"
        );

        Label group = new Label(
                "Group PG2\n\n" +
                        "Mohammed Baquaysh\n" +
                        "William Lind\n" +
                        "Andrii Kryulin"
        );
        group.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox root = new VBox(20, title, course, group);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #e8f4ff;");

        stage.setScene(new Scene(root, 600, 700));
        stage.centerOnScreen();

        PauseTransition timer = new PauseTransition(Duration.seconds(3));
        timer.setOnFinished(event -> showMainMenu());
        timer.play();
    }

    private void showMainMenu() {
        Label title = new Label("TETRIS");
        title.setFont(Font.font(42));

        Button playButton = createMenuButton("Play");
        Button configurationButton = createMenuButton("Configuration");
        Button highScoresButton = createMenuButton("High Scores");
        Button exitButton = createMenuButton("Exit");

        exitButton.setOnAction(event -> showExitConfirmation());

        VBox root = new VBox(
                20,
                title,
                playButton,
                configurationButton,
                highScoresButton,
                exitButton
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #e8f4ff;");

        stage.setScene(new Scene(root, 600, 700));
        stage.centerOnScreen();
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(220, 50);
        button.setFont(Font.font(18));
        return button;
    }

    private void showExitConfirmation() {
        ButtonType yesButton =
                new ButtonType("Yes", ButtonBar.ButtonData.YES);

        ButtonType noButton =
                new ButtonType("No", ButtonBar.ButtonData.NO);

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?",
                yesButton,
                noButton
        );

        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Tetris?");

        ButtonType result = alert.showAndWait().orElse(noButton);

        if (result == yesButton) {
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}