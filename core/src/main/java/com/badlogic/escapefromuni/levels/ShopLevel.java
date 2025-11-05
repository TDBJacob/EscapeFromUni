package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShopLevel extends Level{

    public ShopLevel() {
        mapName = "ShopLevel.tmx";

        sideX = 28;
        sideY = 15;

    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void draw(SpriteBatch batch) {
    }

    @Override
    public boolean collides(com.badlogic.gdx.math.Rectangle rectangle) {
        return false;
    }

}
