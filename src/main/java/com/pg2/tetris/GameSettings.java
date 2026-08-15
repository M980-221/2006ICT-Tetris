package com.pg2.tetris;

public final class GameSettings {
    private int fieldWidth = 10;
    private int fieldHeight = 20;
    private int level = 1;
    private boolean music = true;
    private boolean soundEffects = true;
    private boolean aiPlay = false;
    private boolean extendedMode = false;

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
}
