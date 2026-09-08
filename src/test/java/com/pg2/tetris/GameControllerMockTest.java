package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.Duration;
import org.junit.jupiter.api.Test;
class GameControllerMockTest {
 @Test void mockListenerIsNotCalledAtValidStart(){GameEventListener listener=mock(GameEventListener.class);GameController c=new GameController(new GameSettings(),listener,new PieceSequence(5),new TetrominoFactory());assertFalse(c.isGameOver());verifyNoInteractions(listener);}
 @Test void spyRecordsManualGameOverNotification(){GameEventListener spy=spy(new RecordingListener());spy.onGameOver(250);verify(spy).onGameOver(250);}
 @Test void unreachableExternalColumnNeverBlocks(){GameController c=new GameController(new GameSettings(),null,new PieceSequence(5),new TetrominoFactory());assertTimeoutPreemptively(Duration.ofMillis(250),()->assertFalse(c.moveToColumn(999)));}
 private static final class RecordingListener implements GameEventListener {public void onScoreChanged(int score,int lines){}public void onGameOver(int score){}}
}
