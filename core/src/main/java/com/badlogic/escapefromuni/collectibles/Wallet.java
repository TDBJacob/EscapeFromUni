package com.badlogic.escapefromuni.collectibles;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class Wallet represents the wallet, it has collision, texture, sprite, a list of spawns.
 * It will be instantiated in LibraryFloor2 and its spawns are therefore hard-coded.
 */
public class Wallet {

    Texture texture = new Texture("wallet.png");
    Sprite sprite = new Sprite(texture);
    List<List<Integer>> spawns = this.populateSpawns();
    Rectangle rectangle;
    List<Integer> spawn;
    boolean isDisposed;

    public Wallet() {
        // logic for assigning spawn randomly and creating rectangle
        this.isDisposed = false;
        this.assignSpawn();
        this.sprite.setSize(1, 1);
        // Creates the rectangle for the Wallet around its spawn with the size of the sprite.
        // spawn.get(0) being the x-coordinate and spawn.get(y) being the y-coordinate.
        this.rectangle = new Rectangle(this.spawn.get(0), this.spawn.get(1), 1, 1);
        // Setting the location of the wallet.
        this.sprite.setX(this.spawn.get(0));
        this.sprite.setY(this.spawn.get(1));
    }

    // Populates the spawns for the library card.
    // These are hard-coded as it will spawn on only LibraryFloor1.
    private List<List<Integer>> populateSpawns() {
        List<List<Integer>> spawns = new ArrayList<>();
        List<Integer> spawn1 = List.of(13, 3);
        List<Integer> spawn2 = List.of(37, 3);
        List<Integer> spawn4 = List.of(25, 26);
        spawns.add(spawn1);
        spawns.add(spawn2);
        spawns.add(spawn4);
        return spawns;
    }

    /**
     * This method randomly assigns the spawn for the Wallet.
     */
    private void assignSpawn() {
        Random random = new Random();
        int randomIndex = random.nextInt(this.spawns.size());
        this.spawn = this.spawns.get(randomIndex);
    }

    // Now need to set up the interaction between the player and the Wallet.
    public boolean isCollidingWithPlayer(Player player) {
        return this.rectangle.overlaps(player.getMoneyRectangle());
    }

    /**
     * Updates the logic of the Wallet, to be invoked every frame.
     *
     * @param player is Player player, the player class.
     */
    public void update(Player player) {
        // Interaction between the player and the Wallet checked every frame.
        if (this.isCollidingWithPlayer(player)) {
            this.pickUp(player);
        }
    }

    /**
     * This method is used to simulate the player picking up the Wallet.
     * To be invoked when the player collides with the Wallet.
     *
     * @param player is Player player, the player class.
     */
    private void pickUp(Player player) {
        player.setHasWallet(true);
        this.isDisposed = true;
    }

    /**
     * This draws the Wallet onto the screen.
     * To be invoked every frame, until the Wallet is collected by the player.
     *
     * @param batch is the SpriteBatch.
     */
    public void draw(SpriteBatch batch) {
        // i.e. if the texture has been disposed with don't try to draw it.
        if (!this.isDisposed) {
            this.sprite.draw(batch);
        }
    }
}
