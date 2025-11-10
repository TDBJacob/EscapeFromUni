package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.escapefromuni.entities.Receptionist;
import com.badlogic.escapefromuni.entities.XAxisSlidingEntity;
import com.badlogic.escapefromuni.miscellaneous.Door;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;

import java.util.ArrayList;

/**
 * The LibraryFloor0 class represents the ground floor of the Library level, floor 0.
 */
public class R04_LibraryFloor0 extends Level{

    // Receptionist entity created.
    Texture receptionistTexture = new Texture("entities/receptionist.png");
    Receptionist receptionist = new Receptionist(receptionistTexture, 35, 24, 2f, 1, 1, 34, 36);

    // Door constructed, to block the path of the player.
    Door door = new Door();

    /**
     * Constructs a new LibraryFloor0 with its name (path), in addition to start and end coordinates.
     */
    public R04_LibraryFloor0() {
        // Name of the level.
        this.mapName = "maps/libraryfloor0.tmx";

        levelCoins = new ArrayList<>();
        levelSpeedPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        // Tile that the player spawns at when first entering the level, or tile that takes player to previous level.
        this.startX = 3;
        this.startY = 23;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        this.endX = 4;
        this.endY = 3;

        levelCoins = Level.generateLevelCoins(38, 26); // Needs even int pairs
    }

    // To be invoked in Game to update the entities on this level, when it is the active level.
    public void update(float deltaTime, Player player) {
        // Updates the position and logic of the receptionist.
        this.receptionist.update(deltaTime);
        this.door.update(player);
        this.receptionist.update(deltaTime, player);
    }

    // To be invoked in Game to draw the entities on this level, when it is the active level.
    public void draw(SpriteBatch batch) {
        // Draws the receptionist.
        this.receptionist.draw(batch);
    }

    // To be invoked in Game to check collision between the player sprite and the entities on this level.
    public boolean collides(Player player) {
        Rectangle receptionistRectangle = new Rectangle(receptionist.getSprite().getX(), receptionist.getSprite().getY(), 1,1);
        return receptionistRectangle.overlaps(player.getMoneyRectangle());
    }
}
