package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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

        // Tile that the player spawns at when first entering the level.
        startX = 38;
        startY = 25;

        // Tile that takes player to next level, or starting tile if the player re-enters the level.
        endX = 3;
        endY = 3;
    }

    // These are redundant as there are no entities on floor 3.
    public void update(float deltaTime) {}
    public void draw(SpriteBatch batch) {}
    public boolean collides(Rectangle playerRectangle) {
        return false;
    }
}
