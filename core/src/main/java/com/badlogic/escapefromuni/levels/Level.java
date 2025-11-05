package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

public abstract class Level {

    protected int startX = -1;
    protected int startY = -1;

    protected int sideX = -1;
    protected int sideY = -1;

    // endX and endY are used to spawn the player when they are backtracking, i.e moving back to the previous room
    protected int endX = -1;
    protected int endY = -1;

    protected String mapName;

    protected Level nextLevel = null;
    protected Level sideLevel = null;
    protected Level prevLevel = null;

    public String getMapName() {
        return mapName;
    }

    // To be invoked in Game, in the logic method.
    // This will update the entities on the level.
    public abstract void update(float deltaTime);

    // To be invoked in Game, in the draw method.
    // This will re-draw the entities on the level in their updated positions.
    public abstract void draw(SpriteBatch batch);

    // To be invoked in Game, in the logic method.
    // This will check collision logic between player and entities on the level.
    // Can have general use, i.e. can be expanded to collectibles.
    public abstract boolean collides(com.badlogic.gdx.math.Rectangle rectangle);

    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }

    public int getSideX() {
        return sideX;
    }
    public int getSideY() {
        return sideY;
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

    public Level getSideLevel() {
        return sideLevel;
    }
    public void setSideLevel(Level sideLevel) {
        this.sideLevel = sideLevel;
    }

    public Level getPrevLevel() {
        return prevLevel;
    }
    public void setPrevLevel(Level prevLevel) {
        this.prevLevel = prevLevel;
    }

}
