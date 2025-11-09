package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.powerups.speedPowerup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;

import java.util.ArrayList;

/**
 * Constructs a new MarketSquare04 with its name (path in assets), in addition to start and end coordinates.
 *
 * This level includes a side level called shop so it also has side coordinates
 * if you wish to add a side level such as another shop to another level just include the side coordinates and a
 * "ShopBlock" layer in the tmx file
 */
public class R05_MarketSquare extends Level{

    public R05_MarketSquare() {
        // Name of the level.
        mapName = "maps/Marketsquare.tmx";

        levelCoins = new ArrayList<>();
        levelPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        // Tile that the player spawns at when first entering the level.
        startX = 31;
        startY = 27;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        endX = 36;
        endY = 3;

        // Tile that the player is moved to when entering the side level
        sideX = 3;
        sideY = 3;
        levelCoins = Level.generateLevelCoins(14, 28); // Needs even int pairs
    }
    // These are redundant as there are no entities on floor 3.
    public void update(float deltaTime) {}
    public void draw(SpriteBatch batch) {}
    public boolean collides(Rectangle playerRectangle) {
        return false;
    }
}


