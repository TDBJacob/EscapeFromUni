package com.badlogic.escapefromuni.entities;

import com.badlogic.escapefromuni.ShopStuff.Item;
import com.badlogic.escapefromuni.Timer;
import com.badlogic.escapefromuni.powerups.Powerup;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {

    // Bringing declaration of all player-related values across.
    Texture moneyTexture = new Texture("vecteezy_pack-of-dollars-money-clipart-design-illustration_9391394.png");
    private Sprite moneySprite = new Sprite(this.moneyTexture);
    private Rectangle moneyRectangle;
    private int positiveEventsEncountered = 0;
    private int negativeEventsEncountered = 0;
    private int hiddenEventsEncountered = 0;
    float moneyWidth;
    float moneyHeight;
    // Assign the atlas, and it is final as it does not change.
    private final TextureAtlas atlas = new TextureAtlas();
    // Assign the walkSheet, and it is final as it does not change.
    private final Texture walkSheet = new Texture("prototype_character.png");
    Animation<TextureRegion> stationaryAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    Animation<TextureRegion> rightAnimation;
    // Player starts standing still.
    private String moveDirection = "Stationary";
    private TextureRegion[] stationaryFrames;
    private TextureRegion[] upFrames;
    private TextureRegion[] downFrames;
    private TextureRegion[] rightFrames;
    public Timer gameTimer;
    private boolean collectibleAdded;
    // Stored for use with path blockers.
    private float oldMoneyX;
    private float oldMoneyY;

    // Existing values.
    private float speed;
    // Game start with this speed.
    public final float defaultSpeed = 20f;
    // This will be the list containing all the active power-ups.
    private List<SpeedPowerup> activePowerUps = new ArrayList<>();
    private int coins;
    private List<Item> inventory = new ArrayList<>();
    private boolean hasBirdFeed;
    private boolean hasLibraryCard;
    private boolean hasWallet;

    public Player() {
        this.speed = defaultSpeed;
        this.coins = 0;
        this.hasLibraryCard = false;
        this.populateFrames();
        this.stationaryAnimation = new Animation<TextureRegion>(0.1f, this.stationaryFrames);
        this.upAnimation = new Animation<TextureRegion>(0.025f, this.upFrames);
        this.downAnimation = new Animation<TextureRegion>(0.025f, this.downFrames);
        this.rightAnimation = new Animation<TextureRegion>(0.025f, this.rightFrames);
        this.moneySprite.setSize(0.95f, 0.95f);
        this.moneySprite.setX(40);
        this.moneySprite.setY(27);
        this.moneyRectangle = new Rectangle();
        this.moneyWidth = this.moneySprite.getWidth();
        this.moneyHeight = this.moneySprite.getHeight();
        this.moneyRectangle.setSize(1, 1);
        this.moneyRectangle.x = this.moneySprite.getX();
        this.moneyRectangle.y = this.moneySprite.getY();
    }

    // Getter for timer.
    public Timer getGameTimer() {
        return this.gameTimer;
    }

    // Setter for timer.
    public void setGameTimer(Timer gameTimer) {
        this.gameTimer = gameTimer;
    }

    // Getter for hasWallet.
    public boolean isHasWallet() {
        return this.hasWallet;
    }

    // Setter for hasWallet.
    public void setHasWallet(boolean hasWallet) {
        this.hasWallet = hasWallet;
    }

    // Getter for oldMoneyX.
    public float getOldMoneyX() {
        return this.oldMoneyX;
    }

    // Getter for oldMoneyY.
    public float getOldMoneyY() {
        return this.oldMoneyY;
    }

    // Setter for oldMoneyX.
    public void setOldMoneyX(float oldMoneyX) {
        this.oldMoneyX = oldMoneyX;
    }

    // Setter for oldMoneyY.
    public void setOldMoneyY(float oldMoneyY) {
        this.oldMoneyY = oldMoneyY;
    }

    // Getter for stationaryAnimation.
    public Animation<TextureRegion> getStationaryAnimation() {
        return this.stationaryAnimation;
    }

    // Getter for hasLibraryCard.
    public boolean isHasLibraryCard() {
        return hasLibraryCard;
    }

    // Setter for hasLibraryCard.
    public void setHasLibraryCard(boolean hasLibraryCard) {
        this.hasLibraryCard = hasLibraryCard;
    }

    // Getter for upAnimation.
    public Animation<TextureRegion> getUpAnimation() {
        return this.upAnimation;
    }

    // Getter for downAnimation.
    public Animation<TextureRegion> getDownAnimation() {
        return this.downAnimation;
    }

    // Getter for rightAnimation.
    public Animation<TextureRegion> getRightAnimation() {
        return this.rightAnimation;
    }

    //Getter for moneyWidth.
    public float getMoneyWidth() {
        return this.moneyWidth;
    }

    // Getter for moneyHeight.
    public float getMoneyHeight() {
        return this.moneyHeight;
    }

    // Getter for moneySprite.
    public Sprite getMoneySprite() {
        return this.moneySprite;
    }

    // Getter for moneyRectangle.
    public Rectangle getMoneyRectangle() {
        return this.moneyRectangle;
    }

    // Getter for String moveDirection.
    public String getMoveDirection() {
        return this.moveDirection;
    }

    // Setter for String moveDirection.
    public void setMoveDirection(String moveDirection) {
        this.moveDirection = moveDirection;
    }

    // Getter for positive events.
    public int getPositiveEventsEncountered() {
        return this.positiveEventsEncountered;
    }

    // Getter for negative events.
    public int getNegativeEventsEncountered() {
        return this.negativeEventsEncountered;
    }

    // Getter for hidden events.
    public int getHiddenEventsEncountered() {
        return this.hiddenEventsEncountered;
    }

    // Setter for positive events.
    public void setPositiveEventsEncountered(int positiveEventsEncountered) {
        this.positiveEventsEncountered = positiveEventsEncountered;
    }

    // Setter for negative events.
    public void setNegativeEventsEncountered(int negativeEventsEncountered) {
        this.negativeEventsEncountered = negativeEventsEncountered;
    }

    // Setter for hidden events.
    public void setHiddenEventsEncountered(int hiddenEventsEncountered) {
        this.hiddenEventsEncountered = hiddenEventsEncountered;
    }

    // Getter for walkSheet.
    public Texture getWalkSheet() {
        return this.walkSheet;
    }

    // Getter for atlas.
    public TextureAtlas getAtlas() {
        return this.atlas;
    }

    // Populates stationaryFrames, upFrames, downFrames and rightFrames.
    private void populateFrames() {
        int ssCols = 4;
        int ssRows = 12;

        TextureRegion[][] tmp = TextureRegion.split(this.walkSheet, this.walkSheet.getWidth() / ssCols, this.walkSheet.getHeight() / ssRows);
        // stationaryFrames setup.
        this.stationaryFrames = new TextureRegion[2];
        int index = 0;
        for (int i = 0; i < 2; i ++) {
            this.stationaryFrames[index++] = tmp[0][i];
        }
        // upFrames setup.
        this.upFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i ++) {
            this.upFrames[index++] = tmp[5][i];
        }
        // downFrames setup.
        this.downFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i ++) {
            this.downFrames[index++] = tmp[3][i];
        }
        // rightFrames setup.
        this.rightFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i ++) {
            this.rightFrames[index++] = tmp[4][i];
        }
    }

    // Used for test only
    //public Player(int startingCoins) {
    //this.coins = startingCoins;
    //this.speed = defaultSpeed;
    //}

    public void setHasBirdFeed(){
        this.hasBirdFeed = true;
    }
    public boolean isHasBirdFeed(){
        return this.hasBirdFeed;
    }
    public int getCoins() {
        return coins;
    }
    public void addCoins(int amount) {
        coins += amount;
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addSpeedPowerUp(SpeedPowerup powerUp) {
        powerUp.apply(this);
        activePowerUps.add(powerUp);
    }


    public void increaseSpeed(float multiplier){
        this.speed *= multiplier;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public float getSpeed(){
        return this.speed;
    }

    public void update(float deltaTime) {

        Iterator<SpeedPowerup> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            SpeedPowerup p = iterator.next();
            if (p.update(this, deltaTime)) {
                iterator.remove();
            }
        }

    }
}
