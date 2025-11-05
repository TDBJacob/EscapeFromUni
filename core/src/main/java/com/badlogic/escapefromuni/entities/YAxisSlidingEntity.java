package com.badlogic.escapefromuni.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The YAxisSlidingEntity class is designed to be an entity that is on a slider on the y-axis.
 * It will have a speed, two y-coordinates, a texture and dimensions.
 */
public class YAxisSlidingEntity {
    Texture texture;
    public Sprite sprite;
    // This is the desired movement speed for the SlidingEnemy.
    float speed;
    // These are the y-coordinates, dictating the path of the enemy.
    float y1;
    float y2;
    float initialX;
    float initialY;

    /**
     * Constructs a new YAxisSlidingEntity with speed, sprite, texture, width, height and two points for the enemy to bounce
     * between.
     *
     * @param texture is the desired texture for the entity.
     * @param speed is the desired speed of the entity.
     * @param width duh.
     * @param height duh.
     * @param y1 is one of the two y-coordinates for the enemy to move between. This is the bottom y-coord.
     * @param y2 is one of the two y-coordinates for the enemy to move between. This is the top y-coord.
     */
    public YAxisSlidingEntity(Texture texture, float initialX, float initialY, float speed, float width, float height, int y1, int y2) {
        this.sprite = new Sprite(texture);
        this.speed = speed;
        this.y1 = y1;
        this.y2 = y2;
        this.initialX = initialX;
        this.initialY = initialY;
        this.sprite.setPosition(initialX, initialY);
        this.sprite.setSize(width, height);
    }

    /**
     * This method is called in render every frame, to update the position of the entity using some logic.
     *
     * @param deltaTime is the delta.
     */
    public void update(float deltaTime) {
        // Created the updated y value of the sprite, given the speed it is moving and the time that has passed.
        float updatedY = this.sprite.getY() + (speed * deltaTime);
        // If the y value has passed below y1 then the y value is set to y1 and its speed
        // is reversed.
        if (updatedY < this.y1) {
            updatedY = this.y1;
            this.reverseMovement();
        }
        // If the y value has passed above y2 then the y value is set to y2 and its
        // speed is reversed.
        else if (updatedY + this.sprite.getHeight() + (this.speed * deltaTime) > this.y2) {
            updatedY = this.y2 - this.sprite.getHeight();
            this.reverseMovement();
        }
        this.sprite.setY(updatedY);
    }

    /**
     * This method draws the sprite onto the screen. Called in the draw() method.
     *
     * @param batch is the spriteBatch.
     */
    public void draw(SpriteBatch batch) {
        // Renders the sprite onto the screen using the spriteBatch.
        this.sprite.draw(batch);
    }

    /**
     * Reverses the y-axis speed of the entity.
     */
    public void reverseMovement() {
        this.speed = -this.speed;
    }

    /**
     * Simple getter for Sprite sprite.
     *
     * @return sprite.
     */
    public Sprite getSprite() {
        return this.sprite;
    }
}
