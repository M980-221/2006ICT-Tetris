package com.pg2.tetris;

import javafx.scene.paint.Color;

public enum TetrominoType {
    I(Color.CYAN, new int[][][] {
            {{0,1},{1,1},{2,1},{3,1}},
            {{2,0},{2,1},{2,2},{2,3}},
            {{0,2},{1,2},{2,2},{3,2}},
            {{1,0},{1,1},{1,2},{1,3}}
    }),
    O(Color.GOLD, new int[][][] {
            {{1,0},{2,0},{1,1},{2,1}},
            {{1,0},{2,0},{1,1},{2,1}},
            {{1,0},{2,0},{1,1},{2,1}},
            {{1,0},{2,0},{1,1},{2,1}}
    }),
    T(Color.MEDIUMPURPLE, new int[][][] {
            {{1,0},{0,1},{1,1},{2,1}},
            {{1,0},{1,1},{2,1},{1,2}},
            {{0,1},{1,1},{2,1},{1,2}},
            {{1,0},{0,1},{1,1},{1,2}}
    }),
    S(Color.LIMEGREEN, new int[][][] {
            {{1,0},{2,0},{0,1},{1,1}},
            {{1,0},{1,1},{2,1},{2,2}},
            {{1,1},{2,1},{0,2},{1,2}},
            {{0,0},{0,1},{1,1},{1,2}}
    }),
    Z(Color.RED, new int[][][] {
            {{0,0},{1,0},{1,1},{2,1}},
            {{2,0},{1,1},{2,1},{1,2}},
            {{0,1},{1,1},{1,2},{2,2}},
            {{1,0},{0,1},{1,1},{0,2}}
    }),
    J(Color.DODGERBLUE, new int[][][] {
            {{0,0},{0,1},{1,1},{2,1}},
            {{1,0},{2,0},{1,1},{1,2}},
            {{0,1},{1,1},{2,1},{2,2}},
            {{1,0},{1,1},{0,2},{1,2}}
    }),
    L(Color.ORANGE, new int[][][] {
            {{2,0},{0,1},{1,1},{2,1}},
            {{1,0},{1,1},{1,2},{2,2}},
            {{0,1},{1,1},{2,1},{0,2}},
            {{0,0},{1,0},{1,1},{1,2}}
    });

    private final Color color;
    private final int[][][] rotations;

    TetrominoType(Color color, int[][][] rotations) {
        this.color = color;
        this.rotations = rotations;
    }

    public Color getColor() {
        return color;
    }

    public int[][] cells(int rotation) {
        return rotations[Math.floorMod(rotation, rotations.length)];
    }
}
