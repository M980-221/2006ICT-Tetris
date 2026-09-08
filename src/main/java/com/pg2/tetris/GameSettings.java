package com.pg2.tetris;

public final class GameSettings {
    private int fieldWidth = 10;
    private int fieldHeight = 20;
    private int level = 1;
    private boolean music = true;
    private boolean soundEffects = true;
    private boolean aiPlay = false;
    private boolean extendedMode = false;
    private PlayerType playerOneType = PlayerType.HUMAN;
    private PlayerType playerTwoType = PlayerType.AI;

    public int getFieldWidth() { return fieldWidth; }
    public void setFieldWidth(int fieldWidth) { this.fieldWidth = fieldWidth; }

    public int getFieldHeight() { return fieldHeight; }
    public void setFieldHeight(int fieldHeight) { this.fieldHeight = fieldHeight; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public boolean isMusic() { return music; }
    public void setMusic(boolean music) { this.music = music; }

    public boolean isSoundEffects() { return soundEffects; }
    public void setSoundEffects(boolean soundEffects) { this.soundEffects = soundEffects; }

    public boolean isAiPlay() { return aiPlay; }
    public void setAiPlay(boolean aiPlay) { this.aiPlay = aiPlay; }

    public boolean isExtendedMode() { return extendedMode; }
    public void setExtendedMode(boolean extendedMode) { this.extendedMode = extendedMode; }
    public PlayerType getPlayerOneType() { return playerOneType; }
    public void setPlayerOneType(PlayerType value) { playerOneType = value; }
    public PlayerType getPlayerTwoType() { return playerTwoType; }
    public void setPlayerTwoType(PlayerType value) { playerTwoType = value; }

    public GameSettings copy() {
        GameSettings copy = new GameSettings();
        copy.fieldWidth = fieldWidth; copy.fieldHeight = fieldHeight; copy.level = level;
        copy.music = music; copy.soundEffects = soundEffects; copy.aiPlay = aiPlay;
        copy.extendedMode = extendedMode; copy.playerOneType = playerOneType; copy.playerTwoType = playerTwoType;
        return copy;
    }
}
