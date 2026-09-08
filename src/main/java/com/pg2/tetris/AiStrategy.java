package com.pg2.tetris;

import java.util.Comparator;
import java.util.stream.IntStream;

/** Strategy pattern: chooses the lowest-risk landing using streams and Comparator. */
public final class AiStrategy implements PlayerStrategy {
    private double delay;
    private Tetromino observedPiece;
    @Override public void update(GameController c, double elapsed) {
        if (c.isPaused() || c.isGameOver()) return;
        Tetromino p=c.getCurrentPiece(); if(p==null)return;
        if (p != observedPiece) {
            observedPiece = p;
            delay = .40; // Keep every newly spawned piece visible before moving it.
            return;
        }
        delay -= elapsed; if (delay > 0) return; delay=.13;
        Move best=IntStream.range(0,4).boxed().flatMap(r->IntStream.range(-2,c.getBoard().getColumns()).mapToObj(col->landing(c,p,r,col)))
                .filter(m->m.row()>-99).min(Comparator.comparingInt(Move::cost)).orElse(null);
        if(best==null)return;
        if(p.getRotation()!=best.rotation()) c.rotateClockwise();
        else if(p.getColumn()<best.column()) c.moveRight();
        else if(p.getColumn()>best.column()) c.moveLeft();
        // Descend one visible row at a time. This makes the AI's decisions
        // observable and ensures its score comes from line clears, not an
        // instant hard-drop bonus.
        else c.moveDown();
    }
    private Move landing(GameController c,Tetromino p,int rotation,int col){
        GameBoard b=c.getBoard(); int row=-2;
        if(!b.canPlace(p,row,col,rotation)) return new Move(rotation,col,-100,999999);
        while(b.canPlace(p,row+1,col,rotation))row++;
        int holes=0,bump=0,max=0; int[] heights=new int[b.getColumns()];
        for(int x=0;x<b.getColumns();x++){ boolean block=false; for(int y=0;y<b.getRows();y++){
            boolean occupied=b.getCell(y,x)!=null || contains(p,rotation,col,row,x,y);
            if(occupied&&!block){heights[x]=b.getRows()-y;block=true;} else if(!occupied&&block)holes++;
        } max=Math.max(max,heights[x]); if(x>0)bump+=Math.abs(heights[x]-heights[x-1]); }
        return new Move(rotation,col,row,holes*100+max*5+bump*3-row);
    }
    private boolean contains(Tetromino p,int rot,int col,int row,int x,int y){ for(int[] q:p.getType().cells(rot))if(col+q[0]==x&&row+q[1]==y)return true;return false; }
    private record Move(int rotation,int column,int row,int cost){}
}
