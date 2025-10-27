package com.badlogic.escapefromuni.levels;

public abstract class Level {

    protected int startX;
    protected int startY;

    // endX and endY are used to spawn the player when they are backtracking, i.e moving back to the previous room
    protected int endX;
    protected int endY;

    protected String mapName;

    protected Level nextLevel;
    protected Level prevLevel;

    public String getMapName() {
        return mapName;
    }

    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }
    public int getEndY() {
        return endY;
    }

    public Level getNextLevel() {
        return nextLevel;
    }
    public void setNextLevel(Level nextLevel) {
        this.nextLevel = nextLevel;
    }

    public Level getPrevLevel() {
        return prevLevel;
    }
    public void setPrevLevel(Level prevLevel) {
        this.prevLevel = prevLevel;
    }
}
