package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
class HighScoreRepositoryTest {
 @TempDir Path folder;
 @Test void storesScoresInDescendingOrder() throws Exception {HighScoreRepository r=new HighScoreRepository(folder.resolve("scores.json"));r.add(new HighScore("A",100));r.add(new HighScore("B",500));assertEquals("B",r.findTopTen().get(0).playerName());}
 @Test void retainsOnlyTenScores() throws Exception {HighScoreRepository r=new HighScoreRepository(folder.resolve("scores.json"));for(int i=0;i<12;i++)r.add(new HighScore("P"+i,i));assertEquals(10,r.findTopTen().size());}
}
