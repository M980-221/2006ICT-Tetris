package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
class ScoreRulesTest {
 @ParameterizedTest @CsvSource({"0,0","1,100","2,300","3,600","4,1000","5,0"})
 void awardsRequiredScores(int lines,int expected){assertEquals(expected,ScoreRules.lineClearScore(lines));}
}
