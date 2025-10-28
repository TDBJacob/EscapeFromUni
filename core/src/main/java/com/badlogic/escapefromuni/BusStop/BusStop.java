package com.badlogic.escapefromuni.BusStop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;

/**
 * Bus Stop Game - Collect all coins before time runs out to catch the bus!
 */
public class BusStop implements ApplicationListener {

    // Game constants
    private static final float WORLD_WIDTH = 40f;
    private static final float WORLD_HEIGHT = 30f;
    private static final float PLAYER_SPEED = 10f;
    private static final float COIN_SIZE = 0.8f;
    private static final float PLAYER_SIZE = 1f;
    private static final float BUS_WIDTH = 3f;
    private static final float BUS_HEIGHT = 2f;
    private static final float GAME_TIME = 30f; // 30 seconds to collect all coins
    private static final int TOTAL_COINS = 10;

    // Rendering
    private FitViewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    // Textures
    private Texture playerTexture;
    private Texture coinTexture;
    private Texture busTexture;

    // Game objects
    private Sprite playerSprite;
    private Rectangle playerCollisionBox;
    private Sprite busSprite;
    private Rectangle busCollisionBox;
    private ArrayList<Coin> coins;
    private GameTimer gameTimer;

    // Game state
    private enum GameState {
        PLAYING,
        WON,
        LOST
    }
    private GameState gameState;
    private int coinsCollected;
    private boolean busUnlocked;

    @Override
    public void create() {
        // Initialize rendering components
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);

        // Load textures
        playerTexture = new Texture("Man.png");
        coinTexture = new Texture("vecteezy_pack-of-dollars-money-clipart-design-illustration_9391394.png");
        busTexture = new Texture("Bus.png");

        // Create player
        playerSprite = new Sprite(playerTexture);
        playerSprite.setSize(PLAYER_SIZE, PLAYER_SIZE);
        playerSprite.setPosition(3, 3);
        playerCollisionBox = new Rectangle(playerSprite.getX(), playerSprite.getY(), PLAYER_SIZE, PLAYER_SIZE);

        // Create bus
        busSprite = new Sprite(busTexture);
        busSprite.setSize(BUS_WIDTH, BUS_HEIGHT);
        busSprite.setPosition(WORLD_WIDTH - BUS_WIDTH - 3, WORLD_HEIGHT - BUS_HEIGHT - 3);
        // Don't tint bus gray by default - it will show original colors
        busCollisionBox = new Rectangle(busSprite.getX(), busSprite.getY(), BUS_WIDTH, BUS_HEIGHT);

        // Create coins
        coins = new ArrayList<Coin>();
        createCoins();

        // Initialize game timer
        gameTimer = new GameTimer(GAME_TIME);
        gameTimer.start();

        // Initialize game state
        gameState = GameState.PLAYING;
        coinsCollected = 0;
        busUnlocked = false;
    }

    private void createCoins() {
        // Distribute coins across the game area
        for (int i = 0; i < TOTAL_COINS; i++) {
            float x = MathUtils.random(2f, WORLD_WIDTH - 3f);
            float y = MathUtils.random(2f, WORLD_HEIGHT - 6f);
            
            // Make sure coins don't spawn on player or bus
            while ((x < 6 && y < 6) || (x > WORLD_WIDTH - 10 && y > WORLD_HEIGHT - 8)) {
                x = MathUtils.random(2f, WORLD_WIDTH - 3f);
                y = MathUtils.random(2f, WORLD_HEIGHT - 6f);
            }
            
            Coin coin = new Coin(coinTexture, x, y, COIN_SIZE);
            coins.add(coin);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (width <= 0 || height <= 0) return;
    }

    @Override
    public void render() {
        handleInput();
        update();
        draw();
    }

    private void handleInput() {
        if (gameState != GameState.PLAYING) {
            // Press R to restart
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                restart();
            }
            return;
        }

        float speed = PLAYER_SPEED;
        float delta = Gdx.graphics.getDeltaTime();
        float velX = 0f;
        float velY = 0f;

        // Movement input
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = speed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY = speed * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY = -speed * delta;
        }

        // Diagonal movement correction
        if (Math.abs(velX) > 0.01f && Math.abs(velY) > 0.01f) {
            float root2 = 1.41f;
            velX = velX / root2;
            velY = velY / root2;
        }

        // Apply movement
        playerSprite.translateX(velX);
        playerSprite.translateY(velY);

        // Keep player in bounds
        playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, WORLD_WIDTH - PLAYER_SIZE));
        playerSprite.setY(MathUtils.clamp(playerSprite.getY(), 0, WORLD_HEIGHT - PLAYER_SIZE));

        // Update collision box
        playerCollisionBox.setPosition(playerSprite.getX(), playerSprite.getY());
    }

    private void update() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        float delta = Gdx.graphics.getDeltaTime();

        // Update timer
        gameTimer.update(delta);

        // Check for coin collection
        for (Coin coin : coins) {
            if (!coin.isCollected() && playerCollisionBox.overlaps(coin.getCollisionBox())) {
                coin.collect();
                coinsCollected++;
                
                // Check if all coins collected
                if (coinsCollected >= TOTAL_COINS) {
                    busUnlocked = true;
                    // Add a green tint to indicate bus is ready
                    busSprite.setColor(0.5f, 1f, 0.5f, 1f); // Light green tint
                }
            }
        }

        // Check if player enters bus (win condition)
        if (busUnlocked && playerCollisionBox.overlaps(busCollisionBox)) {
            gameState = GameState.WON;
        }

        // Check if time expired (lose condition)
        if (gameTimer.isExpired() && !busUnlocked) {
            gameState = GameState.LOST;
        }
    }

    private void draw() {
        ScreenUtils.clear(0.2f, 0.3f, 0.2f, 1); // Dark green background

        viewport.apply();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw game objects
        spriteBatch.begin();

        // Draw coins
        for (Coin coin : coins) {
            coin.draw(spriteBatch);
        }

        // Draw bus
        busSprite.draw(spriteBatch);

        // Draw player
        playerSprite.draw(spriteBatch);

        spriteBatch.end();

        // Draw UI
        drawUI();

        // Draw game over screen
        if (gameState == GameState.WON) {
            drawWinScreen();
        } else if (gameState == GameState.LOST) {
            drawLoseScreen();
        }
    }

    private void drawUI() {
        spriteBatch.begin();

        // Draw timer
        font.setColor(Color.WHITE);
        String timeText = "Time: " + gameTimer.getSecondsRemaining() + "s";
        font.draw(spriteBatch, timeText, 1, WORLD_HEIGHT - 1);

        // Draw coin counter
        String coinText = "Coins: " + coinsCollected + "/" + TOTAL_COINS;
        font.draw(spriteBatch, coinText, 1, WORLD_HEIGHT - 3);

        // Draw instructions
        if (!busUnlocked) {
            font.setColor(Color.YELLOW);
            font.draw(spriteBatch, "Collect all coins!", WORLD_WIDTH / 2 - 5, WORLD_HEIGHT - 1);
        } else {
            font.setColor(Color.GREEN);
            font.draw(spriteBatch, "Get to the bus!", WORLD_WIDTH / 2 - 4, WORLD_HEIGHT - 1);
        }

        spriteBatch.end();

        // Draw timer bar
        drawTimerBar();
    }

    private void drawTimerBar() {
        float barWidth = WORLD_WIDTH - 2;
        float barHeight = 0.5f;
        float barX = 1;
        float barY = WORLD_HEIGHT - 5;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        
        // Timer progress
        float percentage = gameTimer.getPercentageRemaining();
        Color barColor = percentage > 0.5f ? Color.GREEN : percentage > 0.25f ? Color.YELLOW : Color.RED;
        shapeRenderer.setColor(barColor);
        shapeRenderer.rect(barX, barY, barWidth * percentage, barHeight);
        
        shapeRenderer.end();
    }

    private void drawWinScreen() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        shapeRenderer.end();

        spriteBatch.begin();
        font.setColor(Color.GREEN);
        font.getData().setScale(3f);
        font.draw(spriteBatch, "LEVEL COMPLETE!", WORLD_WIDTH / 2 - 10, WORLD_HEIGHT / 2 + 3);
        
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "You caught the bus!", WORLD_WIDTH / 2 - 7, WORLD_HEIGHT / 2);
        
        font.getData().setScale(1.5f);
        font.draw(spriteBatch, "Press R to restart", WORLD_WIDTH / 2 - 6, WORLD_HEIGHT / 2 - 3);
        font.getData().setScale(1.5f);
        spriteBatch.end();
    }

    private void drawLoseScreen() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        shapeRenderer.end();

        spriteBatch.begin();
        font.setColor(Color.RED);
        font.getData().setScale(3f);
        font.draw(spriteBatch, "LEVEL FAILED!", WORLD_WIDTH / 2 - 8, WORLD_HEIGHT / 2 + 3);
        
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
        font.draw(spriteBatch, "Time's up!", WORLD_WIDTH / 2 - 4, WORLD_HEIGHT / 2);
        
        font.getData().setScale(1.5f);
        font.draw(spriteBatch, "Coins: " + coinsCollected + "/" + TOTAL_COINS, WORLD_WIDTH / 2 - 4, WORLD_HEIGHT / 2 - 2);
        font.draw(spriteBatch, "Press R to restart", WORLD_WIDTH / 2 - 6, WORLD_HEIGHT / 2 - 4);
        font.getData().setScale(1.5f);
        spriteBatch.end();
    }

    private void restart() {
        // Reset player
        playerSprite.setPosition(3, 3);
        playerCollisionBox.setPosition(playerSprite.getX(), playerSprite.getY());

        // Reset bus
        busSprite.setColor(Color.WHITE); // Reset to original colors
        busUnlocked = false;

        // Reset coins
        coins.clear();
        createCoins();
        coinsCollected = 0;

        // Reset timer
        gameTimer.reset();
        gameTimer.start();

        // Reset game state
        gameState = GameState.PLAYING;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        playerTexture.dispose();
        coinTexture.dispose();
        busTexture.dispose();
    }
}
