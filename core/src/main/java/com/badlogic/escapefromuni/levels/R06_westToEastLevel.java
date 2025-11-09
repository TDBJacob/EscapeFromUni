package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;
import com.badlogic.escapefromuni.powerups.Powerup;
import com.badlogic.escapefromuni.powerups.speedPowerup;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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

        levelCoins = generateLevelCoins(18, 25, 23, 16, 25, 9);
        levelPowerups.add(new speedPowerup(Game.planetTexture, Game.planetSound, 33, 14, 1.25f, 300.0f));
        levelEnemies.add(new Enemy(Game.duckTexture, Game.duckSound, 23, 6, "Duck"));
    }
    public void update(float deltaTime) {}
    public void draw(SpriteBatch batch) {}
    public boolean collides(Rectangle playerRectangle) {return false;}
}
