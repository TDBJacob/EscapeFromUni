package com.badlogic.escapefromuni.BusStop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Coin collectible class for the bus stop game
 */
public class Coin {
    private Sprite sprite;
    private Rectangle collisionBox;
    private boolean isCollected;
    private float x, y;

    public Coin(Texture texture, float x, float y, float size) {
        this.x = x;
        this.y = y;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(size, size);
        this.sprite.setPosition(x, y);
        this.collisionBox = new Rectangle(x, y, size, size);
        this.isCollected = false;
    }

    public void draw(SpriteBatch batch) {
        if (!isCollected) {
            sprite.draw(batch);
        }
    }

    public void collect() {
        isCollected = true;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

