package com.badlogic.escapefromuni.collectibles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;

public class Collectible {
    protected Texture texture;
    protected String textureName;
    public Sound SoundEffect;
    protected float itemX;
    protected float itemY;
    protected Rectangle itemCollision;
    protected Boolean collected = false;

    public Collectible (Texture textureName, Sound sound, float x, float y)
    {
        itemX = x;
        itemY = y;
        texture = textureName;
        SoundEffect = sound;
        itemCollision = new Rectangle(x, y, 1, 1);
    }

    public void render(SpriteBatch batch) {
        if (!collected) {
            batch.draw(texture, itemX, itemY, 1, 1);
        }
    }

    public void collect() {
        collected = true;
    }

    public Boolean isCollected() {
        return collected;
    }

    // Removes the item texture
    public void deleteItem() {
        texture.dispose();
    }

    public Rectangle getCollider() {
        return itemCollision;
    }

 // Takes in an even number of integers, and pairs them to make level coins
    public static ArrayList<Collectible> generateLevelCoins(int... coinCoordinates) {
        ArrayList<Collectible> newCoinList;
        newCoinList = new ArrayList<>();
        for (int i = 1; i <= coinCoordinates.length; i++) {
            if (!((i % 2) == 0)) {
                newCoinList.add(new Collectible(Game.coinTexture, Game.coinSound, coinCoordinates[i-1], coinCoordinates[i]));
            }
        }
        return newCoinList;
    }
}
