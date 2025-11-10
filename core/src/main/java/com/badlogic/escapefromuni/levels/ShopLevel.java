package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;

import java.util.ArrayList;

public class ShopLevel extends Level{

    public ShopLevel() {
        mapName = "ShopLevel.tmx";

        levelCoins = new ArrayList<>();
        levelSpeedPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        startX = 28;
        startY = 15;

        endX = 16;
        endY = 3;

        sideX = 6;
        sideY = 20;

    }

    // These are redundant as there are no entities on shopLevel

    @Override
    public void update(float deltaTime, Player player) {

    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public boolean collides(Player player) {
        return false;
    }
}
