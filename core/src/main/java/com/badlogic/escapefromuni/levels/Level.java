package com.badlogic.escapefromuni.levels;

import java.util.ArrayList;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.collectibles.Collectible;
import com.badlogic.escapefromuni.entities.Enemy;
import com.badlogic.escapefromuni.powerups.Powerup;

import java.util.ArrayList;
import com.badlogic.escapefromuni.Game;

public abstract class Level {

    protected ArrayList<Collectible> levelCoins;
    protected ArrayList<SpeedPowerup> levelSpeedPowerups;
    protected ArrayList<Enemy> levelEnemies;

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

    protected Sprite minimapIcon;

    public String getMapName() {
        return mapName;
    }

    // To be invoked in Game, in the logic method.
    // This will update the entities on the level.
    public abstract void update(float deltaTime, Player player);

    // To be invoked in Game, in the draw method.
    // This will re-draw the entities on the level in their updated positions.
    public abstract void draw(SpriteBatch batch);

    // To be invoked in Game, in the logic method.
    // This will check collision logic between player and entities on the level.
    // Can have general use, i.e. can be expanded to collectibles.
    public abstract boolean collides(Player player);

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

    public Sprite getMinimapSprite() {
        return minimapIcon;
    }
    public void setMinimapSprite(Sprite newSprite) {
        this.minimapIcon = newSprite;
    }

    public ArrayList<Collectible> getLevelCoins() {
        return levelCoins;
    }
    public ArrayList<SpeedPowerup> getLevelPowerups() {
        return levelSpeedPowerups;
    }
    public ArrayList<Enemy> getLevelEnemies() {
        return levelEnemies;
    }

    // Takes in an even number of integers, and pairs them to make level coins
    public static ArrayList<Collectible> generateLevelCoins(int... coinCoordinates) {
        ArrayList<Collectible> newCoinList;
        newCoinList = new ArrayList<>();
        for (int i = 1; i <= coinCoordinates.length; i++) {
            if (!((i % 2) == 0)) {
                newCoinList.add(new Collectible(Game.coinTexture, Game.coinSound, coinCoordinates[i - 1], coinCoordinates[i]));
            }
        }
        return newCoinList;
    }

    /**
     * Optional lifecycle hook invoked when the game switches to this level.
     * Provides the collision layer and collision rectangles for the level so
     * implementations can query map data (for example, to place collectibles).
     * Default implementation does nothing.
     */
    public void onEnter(TiledMapTileLayer mapCollisionLayer, ArrayList<Rectangle> mapCollisions) {
        // default: no-op
    }
}
