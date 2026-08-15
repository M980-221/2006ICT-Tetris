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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public final class TetrisApplication extends Application {

    private Stage stage;

    private final GameSettings settings = new GameSettings();

    private GamePane activeGamePane;

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

        VBox root = new VBox(
                20,
                title,
                course,
                group
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.setStyle(
                "-fx-background-color: #e8f4ff;"
        );

        stage.setScene(
                new Scene(
                        root,
                        600,
                        700
                )
        );

        stage.setWidth(600);
        stage.setHeight(700);

        stage.centerOnScreen();

        PauseTransition timer =
                new PauseTransition(
                        Duration.seconds(3)
                );

        timer.setOnFinished(
                event -> showMainMenu()
        );

        timer.play();
    }

    private void showMainMenu() {

        stopActiveGame();

        Label title =
                new Label("TETRIS");

        title.setFont(
                Font.font(42)
        );

        Button playButton =
                createMenuButton("Play");

        Button configurationButton =
                createMenuButton(
                        "Configuration"
                );

        Button highScoresButton =
                createMenuButton(
                        "High Scores"
                );

        Button exitButton =
                createMenuButton("Exit");

        playButton.setOnAction(
                event -> showGameScreen()
        );

        configurationButton.setOnAction(
                event ->
                        showConfigurationScreen()
        );

        highScoresButton.setOnAction(
                event ->
                        showHighScoresScreen()
        );

        exitButton.setOnAction(
                event ->
                        showExitConfirmation()
        );

        VBox root =
                new VBox(
                        20,
                        title,
                        playButton,
                        configurationButton,
                        highScoresButton,
                        exitButton
                );

        root.setAlignment(Pos.CENTER);

        root.setPadding(
                new Insets(40)
        );

        root.setStyle(
                "-fx-background-color: #e8f4ff;"
        );

        stage.setScene(
                new Scene(
                        root,
                        600,
                        700
                )
        );

        stage.setWidth(600);
        stage.setHeight(700);

        stage.centerOnScreen();
    }

    private void showGameScreen() {

        stopActiveGame();

        activeGamePane =
                new GamePane(
                        settings,
                        this::showMainMenu
                );

        /*
         * Keep the game window at a fixed size.
         *
         * GamePane handles board scaling, so a
         * 40-row board will not make the entire
         * application window extremely tall.
         */
        Scene scene =
                new Scene(
                        activeGamePane,
                        700,
                        760
                );

        stage.setScene(scene);

        stage.setWidth(700);
        stage.setHeight(760);

        stage.centerOnScreen();

        Platform.runLater(
                activeGamePane::requestFocus
        );
    }

    private void showConfigurationScreen() {

        stopActiveGame();

        Label title =
                new Label(
                        "CONFIGURATION"
                );

        title.setFont(
                Font.font(32)
        );

        VBox widthControl =
                createSliderControl(
                        "Field Width",
                        10,
                        20,
                        settings.getFieldWidth(),
                        value ->
                                settings.setFieldWidth(
                                        value
                                )
                );

        VBox heightControl =
                createSliderControl(
                        "Field Height",
                        20,
                        40,
                        settings.getFieldHeight(),
                        value ->
                                settings.setFieldHeight(
                                        value
                                )
                );

        VBox levelControl =
                createSliderControl(
                        "Game Level",
                        1,
                        10,
                        settings.getLevel(),
                        value ->
                                settings.setLevel(
                                        value
                                )
                );

        CheckBox musicCheckBox =
                new CheckBox("Music");

        CheckBox soundEffectsCheckBox =
                new CheckBox(
                        "Sound Effects"
                );

        CheckBox aiPlayCheckBox =
                new CheckBox(
                        "AI Play"
                );

        CheckBox extendedModeCheckBox =
                new CheckBox(
                        "Extended Mode"
                );

        musicCheckBox.setSelected(
                settings.isMusic()
        );

        soundEffectsCheckBox.setSelected(
                settings.isSoundEffects()
        );

        aiPlayCheckBox.setSelected(
                settings.isAiPlay()
        );

        extendedModeCheckBox.setSelected(
                settings.isExtendedMode()
        );

        musicCheckBox
                .selectedProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                settings.setMusic(
                                        newValue
                                )
                );

        soundEffectsCheckBox
                .selectedProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                settings.setSoundEffects(
                                        newValue
                                )
                );

        aiPlayCheckBox
                .selectedProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                settings.setAiPlay(
                                        newValue
                                )
                );

        extendedModeCheckBox
                .selectedProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) ->
                                settings.setExtendedMode(
                                        newValue
                                )
                );

        VBox checkBoxGroup =
                new VBox(
                        12,
                        musicCheckBox,
                        soundEffectsCheckBox,
                        aiPlayCheckBox,
                        extendedModeCheckBox
                );

        checkBoxGroup.setAlignment(
                Pos.CENTER_LEFT
        );

        checkBoxGroup.setMaxWidth(
                300
        );

        Button backButton =
                createMenuButton(
                        "Back"
                );

        backButton.setOnAction(
                event -> showMainMenu()
        );

        VBox root =
                new VBox(
                        20,
                        title,
                        widthControl,
                        heightControl,
                        levelControl,
                        checkBoxGroup,
                        backButton
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(35)
        );

        root.setStyle(
                "-fx-background-color: #e8f4ff;"
        );

        stage.setScene(
                new Scene(
                        root,
                        600,
                        700
                )
        );

        stage.setWidth(600);
        stage.setHeight(700);

        stage.centerOnScreen();
    }

    private VBox createSliderControl(
            String settingName,
            int minimum,
            int maximum,
            int startingValue,
            IntValueConsumer consumer
    ) {

        Label valueLabel =
                new Label(
                        settingName
                                + ": "
                                + startingValue
                );

        Slider slider =
                new Slider(
                        minimum,
                        maximum,
                        startingValue
                );

        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);

        if (
                settingName.equals(
                        "Game Level"
                )
        ) {

            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);

        } else {

            slider.setMajorTickUnit(5);
            slider.setMinorTickCount(4);
        }

        slider.setBlockIncrement(1);

        slider.setMaxWidth(
                300
        );

        slider
                .valueProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> {

                            int value =
                                    newValue.intValue();

                            valueLabel.setText(
                                    settingName
                                            + ": "
                                            + value
                            );

                            consumer.accept(
                                    value
                            );
                        }
                );

        VBox control =
                new VBox(
                        8,
                        valueLabel,
                        slider
                );

        control.setAlignment(
                Pos.CENTER
        );

        return control;
    }

    private void showHighScoresScreen() {

        stopActiveGame();

        Label title =
                new Label(
                        "HIGH SCORES"
                );

        title.setFont(
                Font.font(32)
        );

        List<HighScore> highScores =
                List.of(
                        new HighScore(
                                "AAA",
                                10000
                        ),

                        new HighScore(
                                "BBB",
                                9000
                        ),

                        new HighScore(
                                "CCC",
                                8000
                        ),

                        new HighScore(
                                "DDD",
                                7000
                        ),

                        new HighScore(
                                "EEE",
                                6000
                        ),

                        new HighScore(
                                "FFF",
                                5000
                        ),

                        new HighScore(
                                "GGG",
                                4000
                        ),

                        new HighScore(
                                "HHH",
                                3000
                        ),

                        new HighScore(
                                "III",
                                2000
                        ),

                        new HighScore(
                                "JJJ",
                                1000
                        )
                );

        VBox scoreList =
                new VBox(8);

        scoreList.setAlignment(
                Pos.CENTER
        );

        int position = 1;

        for (
                HighScore highScore :
                highScores
        ) {

            Label scoreLabel =
                    new Label(
                            String.format(
                                    "%2d.  %-5s  %05d",
                                    position,
                                    highScore.playerName(),
                                    highScore.score()
                            )
                    );

            scoreLabel.setFont(
                    Font.font(
                            "Monospaced",
                            18
                    )
            );

            scoreList
                    .getChildren()
                    .add(
                            scoreLabel
                    );

            position++;
        }

        Button backButton =
                createMenuButton(
                        "Back"
                );

        backButton.setOnAction(
                event ->
                        showMainMenu()
        );

        VBox root =
                new VBox(
                        20,
                        title,
                        scoreList,
                        backButton
                );

        root.setAlignment(
                Pos.CENTER
        );

        root.setPadding(
                new Insets(35)
        );

        root.setStyle(
                "-fx-background-color: #e8f4ff;"
        );

        stage.setScene(
                new Scene(
                        root,
                        600,
                        700
                )
        );

        stage.setWidth(600);
        stage.setHeight(700);

        stage.centerOnScreen();
    }

    private Button createMenuButton(
            String text
    ) {

        Button button =
                new Button(text);

        button.setPrefSize(
                220,
                50
        );

        button.setFont(
                Font.font(18)
        );

        return button;
    }

    private void showExitConfirmation() {

        ButtonType yesButton =
                new ButtonType(
                        "Yes",
                        ButtonBar.ButtonData.YES
                );

        ButtonType noButton =
                new ButtonType(
                        "No",
                        ButtonBar.ButtonData.NO
                );

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to exit?",
                        yesButton,
                        noButton
                );

        alert.setTitle(
                "Exit Confirmation"
        );

        alert.setHeaderText(
                "Exit Tetris?"
        );

        ButtonType result =
                alert
                        .showAndWait()
                        .orElse(
                                noButton
                        );

        if (
                result == yesButton
        ) {

            Platform.exit();
        }
    }

    private void stopActiveGame() {

        if (
                activeGamePane != null
        ) {

            activeGamePane.stop();

            activeGamePane = null;
        }
    }

    @Override
    public void stop() {

        stopActiveGame();
    }

    @FunctionalInterface
    private interface IntValueConsumer {

        void accept(
                int value
        );
    }

    public static void main(
            String[] args
    ) {

        launch(args);
    }
}