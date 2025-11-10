package com.badlogic.escapefromuni.powerups;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.escapefromuni.collectibles.Collectible;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import java.util.ArrayList;
import com.badlogic.gdx.audio.Sound;

public interface Powerup {


    public boolean isPowerUpAdded();

    public void setPowerUpAdded(boolean powerUpAdded);

    public void apply(Player player); //called when used


    void remove(Player player); //called when the power up ends (if we want it timed)


    boolean isTemp();

    boolean update(Player player, float deltaTime);

    public void render(SpriteBatch batch);

    public void collect();

    public Boolean isCollected();

    // Removes the item texture
    public void deleteItem();

    public Rectangle getCollider();

    public Sound getSoundEffect();

}
