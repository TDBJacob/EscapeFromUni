package com.badlogic.escapefromuni.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.Game;
import com.badlogic.gdx.audio.Sound;

public class Enemy {
    protected Texture texture;
    protected String textureName;
    public Sound soundEffect;
    protected float enemyX;
    protected float enemyY;
    protected Rectangle enemyCollision;
    protected Boolean isDead = false;
    protected Sprite enemySprite;
    protected String enemyAI;
    protected Boolean showText = false;
    protected float speechMaxLength = 0.5f;
    protected float speechDuration;
    protected Texture speechTexture;

    // Takes in basic enemy info with a default size of 1, 1
    public Enemy(Texture textureName, Sound sound, float x, float y, String aiType) {
        this.enemyX = x;
        this.enemySprite = new Sprite(textureName);
        this.enemyY = y;
        this.texture = textureName;
        this.soundEffect = sound;
        this.enemyCollision = new Rectangle(enemyX, enemyY, 5, 1);
        this.enemyAI = aiType;
        if (aiType == "Duck") {
            this.enemySpeech(Game.duckSpeechBubbleTexture);
        }
    }

    public void render(SpriteBatch batch) {
        if (!isDead) {
            batch.draw(texture, enemyX, enemyY, 1, 1);
        }
    }

    // Generates the speech bubble sprite for talking enemies
    public void renderSpeech(SpriteBatch batch) {
        if ((!isDead) && this.showText) {
            if (this.enemyAI == "Duck") {
                batch.draw(Game.duckSpeechBubbleTexture, this.enemyX + 1, this.enemyY + 1, 4, 4);
            }
        }
    }

    public void collect() {
        isDead = true;
    }

    public Boolean isDead() {
        return isDead;
    }

    public void deleteEnemy() {
        texture.dispose();
    }

    public Rectangle getCollider() {
        return enemyCollision;
    }

    public Sprite getSprite() {
        return enemySprite;
    }

    public String getAIType() {
        return enemyAI;
    }

    public Boolean getShowText() {
        return this.showText;
    }

    // Despawns the duck when requirements are met
    public void enemyBehaviour1() {
        if (Game.Score >= 10) {
            soundEffect.play();
            isDead = true;
            texture.dispose(); // needs to be edited to the birdseed code for buying bird seeds in the shop
        }
        else {
            Game.duckSound2.play();
        }
    }

    // Sets the speech bubble and timer for an enemy's speech
    public void enemySpeech(Texture speechTexture) {
            this.speechTexture = speechTexture;
            this.speechDuration = 0;
            //this.showText = true;
    }

    public void speechTimeCheck(float delta) {
        speechDuration += delta;
        if (speechDuration >= speechMaxLength) {
            showText = false;
        }
    }

    public void enableShowText() {
        this.showText = true;
        this.speechDuration = 0;
    }

    //Reverts player position and performs enemy behaviour, on enemy collision
    public static void enemyCollisionLogic(float oldX, float oldY) {
        for (Enemy enemy : Game.currentLevel.getLevelEnemies()) {
            if (!(enemy.isDead()) && Game.moneyRectangle.overlaps(enemy.getCollider())) {
                enemy.enableShowText();
                if (enemy.getAIType() == "Duck") {
                    enemy.enemyBehaviour1();
                    Game.moneySprite.setPosition(oldX, oldY);
                    Game.moneyRectangle.setPosition(oldX, oldY);
                }
            }
        }
    }
}
