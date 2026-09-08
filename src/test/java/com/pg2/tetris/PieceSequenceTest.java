package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
class PieceSequenceTest {
 @Test void sameSeedProducesSameSequence(){PieceSequence a=new PieceSequence(42),b=new PieceSequence(42);for(int i=0;i<30;i++)assertEquals(a.at(i),b.at(i));}
 @Test void firstBagContainsEveryType(){PieceSequence s=new PieceSequence(1);Set<TetrominoType> set=new HashSet<>();for(int i=0;i<7;i++)set.add(s.at(i));assertEquals(7,set.size());}
}
