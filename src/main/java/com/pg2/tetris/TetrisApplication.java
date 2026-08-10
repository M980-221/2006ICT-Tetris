package com.pg2.tetris;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public final class TetrisApplication extends Application {

    private Stage stage;

    private record HighScore(String playerName, int score) {
    }

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

        group.setTextAlignment(TextAlignment.CENTER);

        VBox root = new VBox(20, title, course, group);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #e8f4ff;");

        stage.setScene(new Scene(root, 600, 700));
        stage.centerOnScreen();

        PauseTransition timer =
                new PauseTransition(Duration.seconds(3));

        timer.setOnFinished(event -> showMainMenu());
        timer.play();
    }

    private void showMainMenu() {
        Label title = new Label("TETRIS");
        title.setFont(Font.font(42));

        Button playButton = createMenuButton("Play");
        Button configurationButton =
                createMenuButton("Configuration");
        Button highScoresButton =
                createMenuButton("High Scores");
        Button exitButton = createMenuButton("Exit");

        configurationButton.setOnAction(
                event -> showConfigurationScreen()
        );

        highScoresButton.setOnAction(
                event -> showHighScoresScreen()
        );

        exitButton.setOnAction(
                event -> showExitConfirmation()
        );

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

    private void showConfigurationScreen() {
        Label title = new Label("CONFIGURATION");
        title.setFont(Font.font(32));

        VBox widthControl =
                createSliderControl("Field Width", 10, 20, 10);

        VBox heightControl =
                createSliderControl("Field Height", 20, 40, 20);

        VBox levelControl =
                createSliderControl("Game Level", 1, 10, 1);

        CheckBox musicCheckBox = new CheckBox("Music");
        CheckBox soundEffectsCheckBox =
                new CheckBox("Sound Effects");
        CheckBox aiPlayCheckBox = new CheckBox("AI Play");
        CheckBox extendedModeCheckBox =
                new CheckBox("Extended Mode");

        musicCheckBox.setSelected(true);
        soundEffectsCheckBox.setSelected(true);

        VBox checkBoxGroup = new VBox(
                12,
                musicCheckBox,
                soundEffectsCheckBox,
                aiPlayCheckBox,
                extendedModeCheckBox
        );

        checkBoxGroup.setAlignment(Pos.CENTER_LEFT);
        checkBoxGroup.setMaxWidth(300);

        Button backButton = createMenuButton("Back");
        backButton.setOnAction(event -> showMainMenu());

        VBox root = new VBox(
                20,
                title,
                widthControl,
                heightControl,
                levelControl,
                checkBoxGroup,
                backButton
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: #e8f4ff;");

        stage.setScene(new Scene(root, 600, 700));
    }

    private VBox createSliderControl(
            String settingName,
            int minimum,
            int maximum,
            int startingValue
    ) {
        Label valueLabel =
                new Label(settingName + ": " + startingValue);

        Slider slider =
                new Slider(minimum, maximum, startingValue);

        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(
                settingName.equals("Game Level") ? 1 : 5
        );
        slider.setBlockIncrement(1);
        slider.setMaxWidth(300);

        slider.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        valueLabel.setText(
                                settingName + ": " +
                                        newValue.intValue()
                        )
        );

        VBox control = new VBox(8, valueLabel, slider);
        control.setAlignment(Pos.CENTER);

        return control;
    }

    private void showHighScoresScreen() {
        Label title = new Label("HIGH SCORES");
        title.setFont(Font.font(32));

        List<HighScore> highScores = List.of(
                new HighScore("AAA", 10000),
                new HighScore("BBB", 9000),
                new HighScore("CCC", 8000),
                new HighScore("DDD", 7000),
                new HighScore("EEE", 6000),
                new HighScore("FFF", 5000),
                new HighScore("GGG", 4000),
                new HighScore("HHH", 3000),
                new HighScore("III", 2000),
                new HighScore("JJJ", 1000)
        );

        VBox scoreList = new VBox(8);
        scoreList.setAlignment(Pos.CENTER);

        int position = 1;

        for (HighScore highScore : highScores) {
            Label scoreLabel = new Label(
                    String.format(
                            "%2d.  %-5s  %05d",
                            position,
                            highScore.playerName(),
                            highScore.score()
                    )
            );

            scoreLabel.setFont(Font.font("Monospaced", 18));
            scoreList.getChildren().add(scoreLabel);
            position++;
        }

        Button backButton = createMenuButton("Back");
        backButton.setOnAction(event -> showMainMenu());

        VBox root = new VBox(
                20,
                title,
                scoreList,
                backButton
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: #e8f4ff;");

        stage.setScene(new Scene(root, 600, 700));
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

        ButtonType result =
                alert.showAndWait().orElse(noButton);

        if (result == yesButton) {
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}