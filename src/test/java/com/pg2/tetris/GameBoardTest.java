package com.pg2.tetris;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class GameBoardTest {
 @Test void emptyBoardAcceptsPiece(){GameBoard b=new GameBoard(10,20);assertTrue(b.canPlace(new StandardTetromino(TetrominoType.O,0,3),0,3,0));}
 @Test void rejectsPieceOutsideLeftBoundary(){GameBoard b=new GameBoard(10,20);assertFalse(b.canPlace(new StandardTetromino(TetrominoType.I,0,-3),0,-3,0));}
 @Test void lockedPieceOccupiesCells(){GameBoard b=new GameBoard(10,20);Tetromino p=new StandardTetromino(TetrominoType.O,0,3);b.lock(p);assertEquals(TetrominoType.O,b.getCell(0,4));}
 @Test void clearsFullRow(){GameBoard b=new GameBoard(4,4);for(int c=0;c<4;c++)b.setCell(3,c,TetrominoType.I);assertEquals(1,b.clearFullRows());for(int c=0;c<4;c++)assertNull(b.getCell(3,c));}
 @Test void clearsMultipleRows(){GameBoard b=new GameBoard(4,4);for(int r=2;r<4;r++)for(int c=0;c<4;c++)b.setCell(r,c,TetrominoType.T);assertEquals(2,b.clearFullRows());}
}
