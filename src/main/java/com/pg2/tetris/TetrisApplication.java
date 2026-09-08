package com.pg2.tetris;

import javafx.animation.PauseTransition;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.List;
import java.util.function.IntConsumer;

/** Application shell/View coordinator. Game rules remain in GameController and GameBoard. */
public final class TetrisApplication extends Application {
    private Stage stage; private final GameSettings settings=SettingsManager.getInstance().get();
    private final HighScoreRepository scores=new HighScoreRepository(); private GamePane activeGame;
    @Override public void start(Stage value){stage=value;stage.setTitle("2006ICT Tetris - Final v9");stage.setResizable(false);AudioManager.getInstance().apply(settings);showSplash();stage.show();}
    private void showSplash(){VBox root=box(new Label("PG2 TETRIS"),new Label("2006ICT Object Oriented Software Development"),new Label("Group PG2\nMohammed Baquaysh\nWilliam Lind\nAndrii Kryulin"));setScene(root,600,700);PauseTransition p=new PauseTransition(Duration.seconds(3));p.setOnFinished(e->showMenu());p.play();}
    private void showMenu(){stopGame();Button play=button("Play",this::showGame);Button config=button("Configuration",this::showConfig);Button high=button("High Scores",this::showScores);Button exit=button("Exit",this::confirmExit);Label title=new Label("TETRIS");title.setFont(Font.font(42));setScene(box(title,play,config,high,exit),600,700);}
    private void showGame(){stopGame();activeGame=new GamePane(settings,this::showMenu);double width=settings.isExtendedMode()?Math.min(1250,settings.getFieldWidth()*52+260):Math.max(600,settings.getFieldWidth()*28+180);double height=Math.min(900,settings.getFieldHeight()*26+180);setScene(activeGame,width,height);Platform.runLater(activeGame::requestFocus);}
    private void showConfig(){
        VBox controls=new VBox(10);controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(slider("Field width",10,20,settings.getFieldWidth(),settings::setFieldWidth),slider("Field height",20,40,settings.getFieldHeight(),settings::setFieldHeight),slider("Starting level",1,10,settings.getLevel(),settings::setLevel));
        CheckBox music=check("Music",settings.isMusic(),settings::setMusic),sound=check("Sound effects",settings.isSoundEffects(),settings::setSoundEffects),two=check("Two-player mode",settings.isExtendedMode(),settings::setExtendedMode);
        ComboBox<PlayerType> p1=playerChoice(settings.getPlayerOneType(),settings::setPlayerOneType),p2=playerChoice(settings.getPlayerTwoType(),settings::setPlayerTwoType);
        controls.getChildren().addAll(music,sound,two,new Label("Player 1 type"),p1,new Label("Player 2 type"),p2,button("Save and Back",()->{AudioManager.getInstance().apply(settings);SettingsManager.getInstance().save();showMenu();}));setScene(controls,600,760);
    }
    private ComboBox<PlayerType> playerChoice(PlayerType current,java.util.function.Consumer<PlayerType> setter){ComboBox<PlayerType> box=new ComboBox<>();box.getItems().addAll(PlayerType.values());box.setValue(current);box.valueProperty().addListener((o,a,b)->setter.accept(b));return box;}
    private CheckBox check(String text,boolean selected,java.util.function.Consumer<Boolean> setter){CheckBox c=new CheckBox(text);c.setSelected(selected);c.selectedProperty().addListener((o,a,b)->setter.accept(b));return c;}
    private VBox slider(String text,int min,int max,int current,IntConsumer setter){Label label=new Label(text+": "+current);Slider s=new Slider(min,max,current);s.setMajorTickUnit(Math.max(1,(max-min)/5.0));s.setShowTickLabels(true);s.setShowTickMarks(true);s.setSnapToTicks(true);s.valueProperty().addListener((o,a,b)->{int v=b.intValue();label.setText(text+": "+v);setter.accept(v);});VBox v=new VBox(3,label,s);v.setMaxWidth(360);return v;}
    private void showScores(){VBox list=box(new Label("TOP 10 HIGH SCORES"));List<HighScore> top=scores.findTopTen();if(top.isEmpty())list.getChildren().add(new Label("No scores yet"));for(int i=0;i<top.size();i++){HighScore s=top.get(i);list.getChildren().add(new Label((i+1)+". "+s.playerName()+" - "+s.score()));}list.getChildren().addAll(button("Clear",()->{Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Clear every high score?",ButtonType.YES,ButtonType.NO);if(a.showAndWait().orElse(ButtonType.NO)==ButtonType.YES)try{scores.clear();showScores();}catch(IOException ex){error(ex);}}),button("Back",this::showMenu));setScene(list,600,700);}
    private void confirmExit(){Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Exit Tetris?",ButtonType.YES,ButtonType.NO);if(a.showAndWait().orElse(ButtonType.NO)==ButtonType.YES)Platform.exit();}
    private Button button(String text,Runnable action){Button b=new Button(text);b.setMinWidth(180);b.setOnAction(e->action.run());return b;}
    private VBox box(Node... nodes){VBox root=new VBox(18,nodes);root.setAlignment(Pos.CENTER);root.setPadding(new Insets(30));root.getStyleClass().add("game-root");return root;}
    private void setScene(Parent root,double width,double height){Scene scene=new Scene(root,width,height);String css=getClass().getResource("tetris.css").toExternalForm();scene.getStylesheets().add(css);stage.setScene(scene);stage.sizeToScene();stage.centerOnScreen();}
    private void stopGame(){if(activeGame!=null){activeGame.stop();activeGame=null;}}
    private void error(Exception e){new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();}
    @Override public void stop(){stopGame();SettingsManager.getInstance().save();}
    public static void main(String[] args){launch(args);}
}
