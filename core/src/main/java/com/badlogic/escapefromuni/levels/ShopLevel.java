package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ShopLevel extends Level{

    public ShopLevel() {
        mapName = "ShopLevel.tmx";

        startX = 0;
        startY = 27;

        endX = 16;
        endY = 0;

        sideX = 28;
        sideY = 15;

    }

    // These are redundant as there are no entities on floor 3.

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public boolean collides(Rectangle rectangle) {
        return false;
    }
}
