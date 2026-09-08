package com.pg2.tetris;

import java.util.*;

/** Deterministic seven-bag source. Each player requests a piece at the same index. */
public final class PieceSequence {
    private final Random random;
    private final List<TetrominoType> pieces = new ArrayList<>();
    public PieceSequence() { this(System.nanoTime()); }
    public PieceSequence(long seed) { random = new Random(seed); }
    public synchronized TetrominoType at(int index) {
        while (pieces.size() <= index) addBag();
        return pieces.get(index);
    }
    private void addBag() {
        List<TetrominoType> bag = new ArrayList<>(List.of(TetrominoType.values()));
        Collections.shuffle(bag, random); pieces.addAll(bag);
    }
}
