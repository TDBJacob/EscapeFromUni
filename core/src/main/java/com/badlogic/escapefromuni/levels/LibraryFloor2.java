package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.entities.XAxisSlidingEntity;
import com.badlogic.escapefromuni.entities.YAxisSlidingEntity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;


/**
 * The LibraryFloor2 class represents the second floor of the Library level, floor 2.
 */
public class LibraryFloor2 extends Level{

    // Sliding enemies created.
    Texture paperTexture = new Texture("entities/paper.png");
    YAxisSlidingEntity paperY = new YAxisSlidingEntity(paperTexture, 9, 23, 8f, 1, 1, 22, 28);
    XAxisSlidingEntity paperX = new XAxisSlidingEntity(paperTexture, 26, 20, 10f, 1, 1, 25, 31);

    /**
     * Constructs a new LibraryFloor2 with its name (path), in addition to start and end coordinates.
     */
    public LibraryFloor2() {
        // Name of the level.
        this.mapName = "maps/libraryfloor2.tmx";

        // Tile that the player spawns at when first entering the level, or tile that takes player to previous level.
        this.startX = 38;
        this.startY = 22;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        this.endX = 4;
        this.endY = 3;
    }

    // To be invoked in Game to update the entities on this level, when it is the active level.
    public void update(float deltaTime) {
        // Updates the position and logic of paperX.
        this.paperX.update(deltaTime);
        // Updates the position and logic of paperY.
        this.paperY.update(deltaTime);
    }

    // To be invoked in Game to draw the entities on this level, when it is the active level.
    public void draw(SpriteBatch batch) {
        // Draws paperX.
        this.paperX.draw(batch);
        // Draws paperY.
        this.paperY.draw(batch);
    }

    // To be invoked in Game to check collision between the player sprite and the entities on this level.
    public boolean collides(com.badlogic.gdx.math.Rectangle playerRectangle) {
        // Construct list of Rectangles for locations of entities.
        List<Rectangle> entityRectangles = new ArrayList<>();
        entityRectangles.add(new Rectangle(paperY.getSprite().getX(), paperY.getSprite().getY(), 1, 1));
        entityRectangles.add(new Rectangle(paperX.getSprite().getX(), paperX.getSprite().getY(), 1, 1));

        // Check if player rectangle collides with entity rectangles.
        for (Rectangle r : entityRectangles) {
            if (r.overlaps(playerRectangle)) {
                return true;
            }
        }
        return false;
    }
}
