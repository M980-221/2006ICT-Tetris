package com.pg2.tetris;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public final class TetrisApplication extends Application {

    @Override
    public void start(Stage stage) {
        Label title = new Label("PG2 Tetris");
        title.setFont(Font.font(32));

        StackPane root = new StackPane(title);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 600, 700);

        stage.setTitle("2006ICT Tetris");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}