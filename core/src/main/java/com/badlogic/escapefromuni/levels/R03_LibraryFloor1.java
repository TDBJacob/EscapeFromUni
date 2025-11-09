package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.entities.XAxisSlidingEntity;
import com.badlogic.escapefromuni.entities.YAxisSlidingEntity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.powerups.speedPowerup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * The LibraryFloor1 class represents the first floor of the Library level, floor 1.
 */
public class R03_LibraryFloor1 extends Level{

    // Instantiate the library card.

    // Create texture for entities.
    Texture paperTexture = new Texture("entities/paper.png");

    // Instantiate XAxisSliding-entities.
    XAxisSlidingEntity paperX1 = new XAxisSlidingEntity(paperTexture, 30, 22, 10f, 1, 1, 29, 35);
    XAxisSlidingEntity paperX2 = new XAxisSlidingEntity(paperTexture, 28, 10, 7f, 1, 1, 27, 32);
    XAxisSlidingEntity paperX3 = new XAxisSlidingEntity(paperTexture, 16, 6, 2f, 1, 1, 15, 19);
    XAxisSlidingEntity paperX4 = new XAxisSlidingEntity(paperTexture, 6, 5, 4f, 1, 1, 5, 9);

    // Instantiate YAxisSliding-entities.
    YAxisSlidingEntity paperY1 = new YAxisSlidingEntity(paperTexture, 25, 15, 5f, 1, 1, 14, 18);
    YAxisSlidingEntity paperY2 = new YAxisSlidingEntity(paperTexture, 14, 23, 6f, 1, 1, 22, 28);
    YAxisSlidingEntity paperY3 = new YAxisSlidingEntity(paperTexture, 5, 23, 3f, 1, 1, 22, 27);

    /**
     * Constructs a new LibraryFloor1 with its name (path), in addition to start and end coordinates.
     */
    public R03_LibraryFloor1() {
        // Name of the level.
        mapName = "maps/libraryfloor1.tmx";

        levelCoins = new ArrayList<>();
        levelPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        // Tile that the player spawns at when first entering the level, or tile that takes player to previous level.
        startX = 38;
        startY = 3;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        endX = 3;
        endY = 3;

        levelCoins = Level.generateLevelCoins(23, 5, 11, 2); // Needs even int pairs
    }

    // To be invoked in Game to update the entities on this level, when it is the active level.
    public void update(float deltaTime) {
        // Update the positions and logic of the entities.
        this.paperX1.update(deltaTime);
        this.paperX2.update(deltaTime);
        this.paperX3.update(deltaTime);
        this.paperX4.update(deltaTime);
        this.paperY1.update(deltaTime);
        this.paperY2.update(deltaTime);
        this.paperY3.update(deltaTime);
    }

    // To be invoked in Game to draw the entities on this level, when it is the active level.
    public void draw(SpriteBatch batch) {
        // Draw the updated entities.
        this.paperX1.draw(batch);
        this.paperX2.draw(batch);
        this.paperX3.draw(batch);
        this.paperX4.draw(batch);
        this.paperY1.draw(batch);
        this.paperY2.draw(batch);
        this.paperY3.draw(batch);
    }

    // To be invoked in Game to check collision between the player sprite and the entities on this level.
    public boolean collides(com.badlogic.gdx.math.Rectangle playerRectangle) {
        // Construct list of rectangles for locations of entities.
        List<Rectangle> entityRectangles = new ArrayList<>();
        entityRectangles.add(new Rectangle(paperX1.getSprite().getX(), paperX1.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperX2.getSprite().getX(), paperX2.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperX3.getSprite().getX(), paperX3.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperX4.getSprite().getX(), paperX4.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperY1.getSprite().getX(), paperY1.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperY2.getSprite().getX(), paperY2.getSprite().getY(), 1,1));
        entityRectangles.add(new Rectangle(paperY3.getSprite().getX(), paperY3.getSprite().getY(), 1,1));

        // Check if the entities collide with the player.
        for (Rectangle r : entityRectangles) {
            if (r.overlaps(playerRectangle)) {
                return true;
            }
        }

        return false;
    }


}
