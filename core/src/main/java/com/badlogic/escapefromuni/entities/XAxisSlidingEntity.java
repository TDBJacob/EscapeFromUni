package com.badlogic.escapefromuni.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * The XAxisSlidingEntity class is designed to be an entity that is on a slider on the x-axis.
 * It will have a speed, two x-coordinates, a texture and dimensions.
 */
public class XAxisSlidingEntity {

    Texture texture;
    Sprite sprite;
    // This is the desired movement speed for the SlidingEnemy.
    float speed;
    // These are the x-coordinates, dictating the path of the enemy.
    float x1;
    float x2;
    float initialX;
    float initialY;

    /**
     * Constructs a new XAxisSlidingEntity with speed, sprite, texture, width, height and two points for the enemy to bounce
     * between.
     *
     * @param texture is the desired texture for the entity.
     * @param speed is the desired speed of the enemy.
     * @param width duh.
     * @param height duh.
     * @param x1 is one of the two x-coordinates for the enemy to move between. This is the left x-coord.
     * @param x2 is one of the two x-coordinates for the enemy to move between. This is the right x-coord.
     */
    public XAxisSlidingEntity(Texture texture, float initialX, float initialY, float speed, float width, float height, int x1, int x2) {
        this.sprite = new Sprite(texture);
        this.speed = speed;
        this.x1 = x1;
        this.x2 = x2;
        this.initialX = initialX;
        this.initialY = initialY;
        this.sprite.setPosition(initialX, initialY);
        this.sprite.setSize(width, height);

        // rectangle constructed given its initial x and y and its width and height.
        //this.rectangle = new Rectangle(this.initialX, this.initialY, width, height);
    }

    /**
     * This method is called in render every frame, to update the position of the entity using some logic.
     *
     * @param deltaTime is the delta.
     */
    public void update(float deltaTime) {
        // Created the updated x value of the sprite, given the speed it is moving and the time that has passed.
        float updatedX = this.sprite.getX() + (speed * deltaTime);
        // If the x value has passed to the left of x1 (i.e. is less than) then the x value is set to x1 and its speed
        // is reversed.
        if (updatedX < this.x1) {
            updatedX = this.x1;
            this.reverseMovement();
        }
        // If the x value has passed to the right of x2 (i.e. is greater than) then the x value is set to x2 and its
        // speed is reversed.
        else if (updatedX + this.sprite.getWidth() + (this.speed * deltaTime) > this.x2) {
            updatedX = this.x2 - this.sprite.getWidth();
            this.reverseMovement();
        }
        this.sprite.setX(updatedX);
    }

    public boolean isCollidingWithPlayer(Rectangle playerRectangle) {
        // Construct the rectangle for the entity.
        Rectangle entityRectangle = new Rectangle(sprite.getX(), sprite.getY(), 1, 1);
        // Return whether it overlaps the playerRectangle.
        return entityRectangle.overlaps(playerRectangle);
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
     * Reverses the x-axis speed of the entity.
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
