package com.badlogic.escapefromuni.miscellaneous;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Door {

    // Instantiate rectangle to cover desired section of map to block.
    Rectangle rectangle;


    public Door() {
        // instantiate rectangle to block path.
        this.rectangle = new Rectangle(3, 3, 3, 1);
    }

    // Checks for collision between player and door.
    public boolean collides(Player player) {
        return rectangle.overlaps(player.getMoneyRectangle());
    }

    // Disallows the collision between player and wall.
    public void disallowCollision(Player player) {
        player.getMoneySprite().setX(player.getOldMoneyX());
        player.getMoneySprite().setY(player.getOldMoneyY());
    }

    // Invoked every frame, allows player to pass if hasLibraryCard, else collision is active.
    public void update(Player player) {
        if (this.collides(player)) {
            if (!player.isHasLibraryCard()) {
                this.disallowCollision(player);
                player.setNegativeEventsEncountered(player.getNegativeEventsEncountered() + 1);
            }
        }
    }
}
