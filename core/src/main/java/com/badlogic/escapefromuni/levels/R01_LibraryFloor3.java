package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;

import java.util.ArrayList;

/**
 * The LibraryFloor3 class represents the third floor of the Library level, floor 3.
 */
public class R01_LibraryFloor3 extends Level{

    /**
     * Constructs a new LibraryFloor3 with its name (path in assets), in addition to start and end coordinates.
     */
    public R01_LibraryFloor3() {
        // Name of the level.
        mapName = "maps/libraryfloor3.tmx";

        levelCoins = new ArrayList<>();
        levelSpeedPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        levelCoins = Level.generateLevelCoins(14, 20, 14, 8); // Needs even int pairs
        levelSpeedPowerups.add(new SpeedPowerup(Game.planetTexture, Game.planetSound, 25, 15, 1.5f, 300.0f));
        //levelEnemies.add(new Enemy(Game.duckTexture, Game.duckSound, 14, 12, "Duck"));

        // Tile that the player spawns at when first entering the level.
        startX = 38;
        startY = 25;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        endX = 3;
        endY = 3;
    }

    // These are redundant as there are no entities on floor 3.
    public void update(float deltaTime, Player player) {}
    public void draw(SpriteBatch batch) {}
    public boolean collides(Player player) {
        return false;
    }
}
