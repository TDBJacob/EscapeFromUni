package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BusLevel extends Level {

    public BusLevel() {
        mapName = "Bus.tmx";

        startX = 20;
        startY = 1;

        endX = 20;
        endY = 1;
    }

    @Override
    public void update(float deltaTime) {
        // Bus level entities are handled in Game.java
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Bus level entities are drawn in Game.java
    }

    @Override
    public boolean collides(Rectangle playerRectangle) {
        // Bus level collisions are handled in Game.java
        return false;
    }
}

