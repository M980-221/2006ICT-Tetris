package com.pg2.tetris;

public enum PlayerType {
    HUMAN("Human"), AI("AI"), EXTERNAL("External");
    private final String label;
    PlayerType(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
