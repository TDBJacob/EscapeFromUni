package com.badlogic.escapefromuni.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class R06_westToEastLevel extends Level{

    public R06_westToEastLevel() {

        levelCoins = new ArrayList<>();
        levelPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        mapName = "West_to_east_map_v2.tmx";

        startX = 17;
        startY = 28;

        endX = 19;
        endY = 0;

        nextLevel = null;
        prevLevel = null;
    }
    public void update(float deltaTime) {}
    public void draw(SpriteBatch batch) {}
    public boolean collides(Rectangle playerRectangle) {return false;}
}
