package com.badlogic.escapefromuni.levels;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.escapefromuni.BusStop.Coin;
import com.badlogic.escapefromuni.BusStop.GameTimer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.escapefromuni.powerups.speedPowerup;
import com.badlogic.escapefromuni.Game;
import com.badlogic.escapefromuni.entities.Enemy;
import com.badlogic.escapefromuni.Game;

/**
 * The BusLevel class represents the bus stop level where players need to collect coins
 * before catching the bus.
 */
public class BusLevel extends Level {

    // Create textures for entities
    private final Texture busTexture = new Texture("Bus.png");
    private final Texture coinTexture = new Texture("vecteezy_pack-of-dollars-money-clipart-design-illustration_9391394.png");

    // Create game entities
    private final Sprite busSprite;
    private final List<Coin> coins;
    // Available walkable tiles (populated onEnter)
    private final List<int[]> availableTiles = new ArrayList<>();
    private final GameTimer gameTimer;
    private boolean busUnlocked;
    private int coinsCollected;
    private final BitmapFont font;

    // Game constants
    private static final float COIN_SIZE = 1f; // 1 tile size
    private static final int TOTAL_COINS = 10;
    private static final float GAME_TIME = 30f;
    // Bus schedule (hidden for 15s, visible for 10s)
    private static final float BUS_HIDDEN_SECONDS = 15f;
    private static final float BUS_VISIBLE_SECONDS = 10f;

    private enum BusVisState { HIDDEN, VISIBLE }
    private BusVisState busVisState = BusVisState.HIDDEN;
    private float busVisTimer = 0f;

    /**
     * Constructs a new BusLevel with its name (path), start and end coordinates,
     * and initializes the bus stop game elements.
     */
    public BusLevel() {
        levelCoins = new ArrayList<>();
        levelPowerups = new ArrayList<>();
        levelEnemies = new ArrayList<>();

        // Name of the level
        mapName = "Bus.tmx";

        // Tile that the player spawns at when first entering the level
        startX = 18;
        startY = 1;

        // Tile that takes player to next level
        endX = 22;
        endY = 28;

        // Initialize bus sprite
        busSprite = new Sprite(busTexture);
        busSprite.setPosition(22, 28); // Position bus near the end
        busSprite.setSize(3, 3); // Make bus 2 tiles wide, 1 tile high

    // Initialize coins (actual placement happens when level is entered and map data is available)
    coins = new ArrayList<>();

        // Initialize game timer
        gameTimer = new GameTimer(GAME_TIME);
        gameTimer.start();

        // Initialize font for UI
        font = new BitmapFont();

        // Initialize game state
        coinsCollected = 0;
        busUnlocked = false;
        // start with bus hidden and timer at 0 (will become visible after BUS_HIDDEN_SECONDS)
        busVisState = BusVisState.HIDDEN;
        busVisTimer = 0f;

        levelPowerups.add(new speedPowerup(Game.planetTexture, Game.planetSound, 36, 16, 1.25f, 300.0f));
    }
    /**
     * Populate coins using the available walkable tiles. If availableTiles is empty,
     * falls back to a simple random distribution.
     */
    private void createCoins() {
        coins.clear();

        if (!availableTiles.isEmpty()) {
            // pick TOTAL_COINS unique tiles from availableTiles
            int max = availableTiles.size();
            java.util.HashSet<Integer> chosen = new java.util.HashSet<>();
            while (chosen.size() < Math.min(TOTAL_COINS, max)) {
                chosen.add(MathUtils.random(0, max - 1));
            }
            for (int idx : chosen) {
                int[] t = availableTiles.get(idx);
                float x = t[0];
                float y = t[1];
                coins.add(new Coin(coinTexture, x, y, COIN_SIZE));
            }
        } else {
            // fallback: random placement in a safe range
            for (int i = 0; i < TOTAL_COINS; i++) {
                float x = MathUtils.random(5, 30); // Adjust range based on your map
                float y = MathUtils.random(5, 25); // Adjust range based on your map
                // Avoid spawning coins near player start or bus
                while ((x < startX + 3 && y < startY + 3) ||
                       (x > busSprite.getX() - 3 && y > busSprite.getY() - 3)) {
                    x = MathUtils.random(5, 30);
                    y = MathUtils.random(5, 25);
                }
                coins.add(new Coin(coinTexture, x, y, COIN_SIZE));
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        // Update game timer
        gameTimer.update(deltaTime);

        // Update bus visibility schedule
        busVisTimer += deltaTime;
        if (busVisState == BusVisState.HIDDEN) {
            if (busVisTimer >= BUS_HIDDEN_SECONDS) {
                busVisState = BusVisState.VISIBLE;
                busVisTimer = 0f;
            }
        } else { // VISIBLE
            if (busVisTimer >= BUS_VISIBLE_SECONDS) {
                busVisState = BusVisState.HIDDEN;
                busVisTimer = 0f;
            }
        }

        // coin collection is handled in collides(playerRectangle) where we get the exact player position

        // Check if timer expired
        if (gameTimer.isExpired() && !busUnlocked) {
            // Handle game over - could reset level or show failure message
            restart();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw bus only when visible
        if (busVisState == BusVisState.VISIBLE) {
            busSprite.draw(batch);
        }

        // Draw coins
        for (Coin coin : coins) {
            if (!coin.isCollected()) {
                coin.draw(batch);
            }
        }

        // Draw coin counter
        font.draw(batch, coinsCollected + "/" + TOTAL_COINS + " coins", 10, 470);
    }

    @Override
    public boolean collides(Rectangle playerRectangle) {
        // First, check coin collection using the actual player rectangle
        for (Coin coin : coins) {
            if (!coin.isCollected() && playerRectangle.overlaps(coin.getCollisionBox())) {
                coin.collect();
                Game.coinSound.play();
                coinsCollected++;
                if (coinsCollected >= TOTAL_COINS) {
                    busUnlocked = true;
                    busSprite.setColor(0.5f, 1f, 0.5f, 1f);
                }
            }
        }

        // Then check if player collides with bus when it's unlocked and the bus is currently visible
        if (busUnlocked && busVisState == BusVisState.VISIBLE) {
            Rectangle busRect = new Rectangle(busSprite.getX(), busSprite.getY(),
                    busSprite.getWidth(), busSprite.getHeight());
            if (busRect.overlaps(playerRectangle)) {
                // Player has reached the bus - level complete!
                return true;
            }
        }

        return false;
    }

    private void restart() {
        // Reset bus
        busSprite.setColor(1, 1, 1, 1); // Reset to white
        busUnlocked = false;

        // Reset coins
        coins.clear();
        createCoins();
        coinsCollected = 0;

        // Reset timer
        gameTimer.reset();
        gameTimer.start();
    }

    @Override
    public void onEnter(TiledMapTileLayer mapCollisionLayer, java.util.ArrayList<Rectangle> mapCollisions) {
        // Build availableTiles list from the map collision layer: tiles with no cell are walkable (0)
        availableTiles.clear();
        if (mapCollisionLayer == null) return;
        int w = mapCollisionLayer.getWidth();
        int h = mapCollisionLayer.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                TiledMapTileLayer.Cell cell = mapCollisionLayer.getCell(x, y);
                if (cell == null) {
                    availableTiles.add(new int[]{x, y});
                }
            }
        }

        // Now that we have available tiles, place coins
        createCoins();
    }
}

