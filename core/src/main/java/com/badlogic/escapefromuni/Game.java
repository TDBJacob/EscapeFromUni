package com.badlogic.escapefromuni;

import com.badlogic.escapefromuni.levels.*;
import com.badlogic.escapefromuni.powerups.speedPowerup;
import com.badlogic.escapefromuni.powerups.Powerup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.escapefromuni.collectibles.Collectible;
import com.badlogic.escapefromuni.entities.Enemy;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {

    public boolean gameEnded;
    public static int Score;
    public String WinOrLose;
    public Timer gameTimer;

    final float root2 = 1.41f;

    float minimapTileSize = 1.4f;

    Player player = new Player();

    ArrayList<Level> levels;

    TiledMap map; // define map
    OrthogonalTiledMapRenderer mapRenderer; // define map renderer
    FitViewport viewport;
    OrthographicCamera camera;
    Texture moneyTexture;
    public static Sprite moneySprite;
    public static Rectangle moneyRectangle;
    SpriteBatch spriteBatch;

    FitViewport uiViewport;
    OrthographicCamera uiCamera;

    int positiveEventsEncountered;
    int negativeEventsEncountered;
    int hiddenEventsEncountered;

    public static Level currentLevel;

    float unitScale;

    TiledMapTileLayer collisionObjectLayer;
    MapObjects objects;

    float moneyWidth;
    float moneyHeight;

    public static Sound coinSound;
    public static Sprite coinSprite;
    public static Texture coinTexture;
    public static Sound planetSound;
    public static Sprite planetSprite;
    public static Texture planetTexture;
    public static Sound duckSound;
    public static Sound duckSound2;
    public static Texture duckTexture;
    public static Texture duckSpeechBubbleTexture;
    public static float speed = 16f;
    public static float money = 0;

    TiledMapTileLayer mapCollisionLayer;
    ArrayList<Rectangle> mapCollisions;

    TiledMapTileLayer mapExitBackLayer;
    ArrayList<Rectangle> mapExitBackCollisions;

    TiledMapTileLayer mapExitForwardLayer;
    ArrayList<Rectangle> mapExitForwardCollisions;

    TiledMapTileLayer mapExitSideLayer;
    ArrayList<Rectangle> mapExitSideCollisions;

    //added for shop ui logic
    TiledMapTileLayer mapShopLayer;
    ArrayList<Rectangle> mapShopCollisions;

    Texture emptyMinimapIcon;
    Texture playerMinimapIcon;
    float minimapBottomHeight;

    BitmapFont font;
    BitmapFont smallFont;
    GlyphLayout layout;

    TextureAtlas atlas;
    Texture walkSheet;
    Animation<TextureRegion> stationaryAnimation;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    Animation<TextureRegion> rightAnimation;

    float stateTime;

    String moveDirection;

    //used by will for side level
    //Level level2;

    ArrayList<Sprite> minimapSprites;

    private BitmapFont genFont(int size) {
        BitmapFont tempFont = new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderWidth = 1;
        parameter.borderColor = com.badlogic.gdx.graphics.Color.BLACK;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear; // smooth scaling up
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear; // smooth scaling down
        tempFont = generator.generateFont(parameter);
        generator.dispose();
        return tempFont;
    }

    // Runs at start
    public Game() {

        WinOrLose = "Return"; // Should be "Return"
        gameEnded = false;
        Score = 0; // Maybe issues with old lowercase "score" which needs to be replaced
        gameTimer = new Timer(5*60);

        positiveEventsEncountered = 0;
        negativeEventsEncountered = 0;
        hiddenEventsEncountered = 0;

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin-drop-422703.mp3"));
        coinTexture = new Texture("Custom_coin_sprite.png");
        coinSprite = new Sprite(coinTexture);

        planetSound = Gdx.audio.newSound(Gdx.files.internal("laser-90052.mp3"));
        planetTexture = new Texture("Neptune_planet_v2.png");
        planetSprite = new Sprite(planetTexture);

        duckSound = Gdx.audio.newSound(Gdx.files.internal("duck-quack-112941.mp3"));
        duckSound2 = Gdx.audio.newSound(Gdx.files.internal("short-beep-351721.mp3"));
        duckTexture = new Texture("custom_duck.png");
        duckSpeechBubbleTexture = new Texture("Custom_speech_bubble_v2.png");

        moveDirection = "Stationary";

        walkSheet = new Texture("prototype_character.png");
        atlas = new TextureAtlas();

        int ssCols = 4;
        int ssRows = 12;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
            walkSheet.getWidth() / ssCols,
            walkSheet.getHeight() / ssRows);

        TextureRegion[] stationaryFrames = new TextureRegion[2];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            stationaryFrames[index++] = tmp[0][i];
        }
        TextureRegion[] upFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i++) {
            upFrames[index++] = tmp[5][i];
        }
        TextureRegion[] downFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i++) {
            downFrames[index++] = tmp[3][i];
        }
        TextureRegion[] rightFrames = new TextureRegion[4];
        index = 0;
        for (int i = 0; i < 4; i++) {
            rightFrames[index++] = tmp[4][i];
        }

        stationaryAnimation = new Animation<TextureRegion>(0.1f, stationaryFrames);
        upAnimation = new Animation<TextureRegion>(0.025f, upFrames);
        downAnimation = new Animation<TextureRegion>(0.025f, downFrames);
        rightAnimation = new Animation<TextureRegion>(0.025f, rightFrames);

        // IMPORTANT: This is the list of levels, the player can traverse back and forth in this order.
        //            Add appropriate exits forward and/or backward in the tilemap on their individual layers.
        levels = new ArrayList<Level>(Arrays.asList(new R01_LibraryFloor3(), new R02_LibraryFloor2(), new R03_LibraryFloor1(), new R04_LibraryFloor0(), new R05_MarketSquare(), new westToEastLevel(), new BusLevel()));

        emptyMinimapIcon = new Texture("emptyminimap.png");
        playerMinimapIcon = new Texture("occupiedminimap.png");
        minimapBottomHeight = 22;

        // This sets the next and previous level attributes of the room objects for ease of use
        for (int i = 0; i < levels.size(); i++){
            levels.get(i).setMinimapSprite(new Sprite(emptyMinimapIcon));
            levels.get(i).getMinimapSprite().setX(38f);
            levels.get(i).getMinimapSprite().setSize(minimapTileSize-0.1f,minimapTileSize-0.1f);
            if (i-1>=0){
                levels.get(i).setPrevLevel(levels.get(i-1));
            }
            if (i+1<levels.size()){
                levels.get(i).setNextLevel(levels.get(i+1));
            }
        }

        // Set up misc. side level stuff here

        // Jacob: Currently set up for ShopLevel instead of level 2
        Level ShopLevel = new ShopLevel();
        levels.get(4).setSideLevel(ShopLevel);
        ShopLevel.setSideLevel(levels.get(4));

        ShopLevel.setMinimapSprite(new Sprite(emptyMinimapIcon));
        ShopLevel.getMinimapSprite().setX(38f-minimapTileSize);
        ShopLevel.getMinimapSprite().setSize(minimapTileSize-0.1f,minimapTileSize-0.1f);
        ShopLevel.setNextLevel(levels.get(5));


        // Generate font and layout
        layout = new GlyphLayout();
        font = genFont(90);
        smallFont = genFont(30);


        // The player always starts at the first level in the array.
        currentLevel = levels.get(0);

        viewport = new FitViewport(40, 30);
        // Starting level is floor 0 of the library (a tmx).
        map = new TmxMapLoader().load("maps/libraryfloor0.tmx");
        unitScale = 1/ 16f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 40, 30);
        mapRenderer.setView(camera);
        moneyTexture = new Texture("vecteezy_pack-of-dollars-money-clipart-design-illustration_9391394.png");
        moneySprite = new Sprite(moneyTexture);
        // Slightly smaller than a tile to allow for easier movement between 1 tile wide gaps
        moneySprite.setSize(0.95f, 0.95f);
        moneySprite.setX(40);
        moneySprite.setY(27);
        moneyRectangle = new Rectangle();
        spriteBatch = new SpriteBatch();

        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiCamera);
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        switchToLevel(currentLevel, "Forward");

        // store the bucket size for brevity
        moneyWidth = moneySprite.getWidth();
        moneyHeight = moneySprite.getHeight();

        moneyRectangle.setSize(1,1);
        moneyRectangle.x = moneySprite.getX();
        moneyRectangle.y = moneySprite.getY();

        stateTime = 0f;
    }

    private ArrayList<String> mapLayersToList(MapLayers mapLayers) {
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < mapLayers.size(); i++) {
            tempList.add(mapLayers.get(i).getName());
        }
        return tempList;
    }

    // Switches to a new map and moves the player appropriately when entering a new room.
    public void switchToLevel(Level newLevel, String enterDirection) {
        // Check if newLevel is null to prevent NullPointerException
        if (newLevel == null) {
            return;
        }

        // Prepare your application here.
        currentLevel = newLevel;


        newLevel.getMinimapSprite().setTexture(playerMinimapIcon);

        map = new TmxMapLoader().load(newLevel.getMapName());
        mapRenderer.setMap(map);
        if (enterDirection == "Forward") {
            moneySprite.setX(newLevel.getStartX());
            moneySprite.setY(newLevel.getStartY());
            if (newLevel.getPrevLevel() != null) {
                newLevel.getPrevLevel().getMinimapSprite().setTexture(emptyMinimapIcon);
            }
        } else if (enterDirection == "Side") {
            moneySprite.setX(newLevel.getSideX());
            moneySprite.setY(newLevel.getSideY());
            if (newLevel.getSideLevel() != null) {
                newLevel.getSideLevel().getMinimapSprite().setTexture(emptyMinimapIcon);
            }
        } else if (enterDirection == "Back") {
            moneySprite.setX(newLevel.getEndX());
            moneySprite.setY(newLevel.getEndY());
            if (newLevel.getNextLevel() != null) {
                newLevel.getNextLevel().getMinimapSprite().setTexture(emptyMinimapIcon);
            }
        }

        // Handle both "Collision" and "collision" layer names
        if (mapLayersToList(map.getLayers()).contains("Collision")) {
            mapCollisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        } else if (mapLayersToList(map.getLayers()).contains("collision")) {
            mapCollisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
        } else {
            // Default to first layer if collision layer not found
            mapCollisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
        }

        viewport.setWorldSize(mapCollisionLayer.getWidth(),mapCollisionLayer.getHeight());

        // Handle ExitBack layer (may not exist in all maps)
        if (mapLayersToList(map.getLayers()).contains("ExitBack")) {
            mapExitBackLayer = (TiledMapTileLayer) map.getLayers().get("ExitBack");
            mapExitBackCollisions = createCollisionRects(mapExitBackLayer);
        } else {
            mapExitBackLayer = null;
            mapExitBackCollisions = new ArrayList<Rectangle>();
        }

        // Handle ExitForward layer (may not exist in all maps)
        if (mapLayersToList(map.getLayers()).contains("ExitForward")) {
            mapExitForwardLayer = (TiledMapTileLayer) map.getLayers().get("ExitForward");
            mapExitForwardCollisions = createCollisionRects(mapExitForwardLayer);
        } else {
            mapExitForwardLayer = null;
            mapExitForwardCollisions = new ArrayList<Rectangle>();
        }

        //check for ExitSide
        if (mapLayersToList(map.getLayers()).contains("ExitSide")) {
            mapExitSideLayer = (TiledMapTileLayer) map.getLayers().get("ExitSide");
            mapExitSideCollisions = createCollisionRects(mapExitSideLayer);
        } else {
            mapExitSideLayer = null;
            mapExitSideCollisions = new ArrayList<Rectangle>();
        }

        //same logic for shopblock layer
        if (mapLayersToList(map.getLayers()).contains("ShopBlock")) {
            mapShopLayer = (TiledMapTileLayer) map.getLayers().get("ShopBlock");
            mapShopCollisions = createCollisionRects(mapShopLayer);
        } else {
            mapShopLayer = null;
            mapShopCollisions = new ArrayList<Rectangle>();
        }

        // mapCollisions will be used to collide, update when switching map.
        mapCollisions = createCollisionRects(mapCollisionLayer);
    // Notify the level that it has been entered so it can access map collision data
    newLevel.onEnter(mapCollisionLayer, mapCollisions);
    }

    // Constructs an ArrayList of all collision rectangles for the layer provided
    private ArrayList<Rectangle> createCollisionRects(TiledMapTileLayer layer) {
        ArrayList<Rectangle> tempRectArray = new ArrayList<Rectangle>();

        // Iterate over every tile, construct a rectangle around the tile, and add to arraylist
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y); // Retrieve cell at our x and y

                if (cell == null) // If nothing here, skip over it
                    continue;

                // Construct rectangle at this tile position
                Rectangle tileRect = new Rectangle(x, y, 1, 1);

                // Add this rectangle to our array
                tempRectArray.add(tileRect);
            }
        }

        return tempRectArray;
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
        uiViewport.update(width, height, true);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    //render is called in main

    public void input() {
        float delta = Gdx.graphics.getDeltaTime(); // Change in time between frames
        player.update(delta); //called to check for active powerups

        // We will use these variables to allow for consistent speed on diagonal movement.
        float velX = 0f;
        float velY = 0f;

        float moneyOldX = moneySprite.getX();
        float moneyOldY = moneySprite.getY();

        //experiment of power up
        //a key press (P) will be used to give the power up instead of a gui button for now
        //higher than 1.5 X speed crashes the collision logic and breaks the game
        if (Gdx.input.isKeyPressed((Input.Keys.E)) ){
            Powerup drinkPowerUp = new speedPowerup(null, null, 0, 0, 1.1f, 5.0f);
            player.addPowerUp(drinkPowerUp);
        }

        String oldMoveDir = moveDirection;
        boolean isMoving = false;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = player.getSpeed() * delta; // Convert to speed/s for consistent gameplay on different FPS
            moveDirection = "Right";
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -player.getSpeed() * delta;
            moveDirection = "Left";
            isMoving = true;
        }
        // Use if here rather than else if, so movement can happen on both axis at once
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY = player.getSpeed() * delta;
            moveDirection = "Up";
            isMoving = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY = -player.getSpeed() * delta;
            moveDirection = "Down";
            isMoving = true;
        }
        if (!isMoving) {
            moveDirection = "Stationary";
        }
        if (moveDirection != oldMoveDir) {
            stateTime = 0f;
        }

        // If the player moves diagonally, we need to divide their speed by root 2 to maintain the correct speed
        // in the direction of motion.
        if (Math.abs(velX) > 0.01f && Math.abs(velY) > 0.01f) {
            velX = velX / root2;
            velY = velY / root2;
        }

        // We will use this rectangle as a preview, to see if the player would collide if they moved
        // as they are trying to.
        // If they would collide, disallow the movement in that axis.
        Rectangle tRect = new Rectangle();

        // NOTE: kind of flawed but won't be noticeable unless very high speed or very low fps,
        // as it doesn't make you flush against a wall if you move into it

        tRect.set(moneySprite.getX()+velX, moneySprite.getY(), moneyWidth, moneyHeight);
        if (!wallCollisionCheck(tRect)) {
            moneySprite.translateX(velX);
        }

        tRect.set(moneySprite.getX(), moneySprite.getY()+velY, moneyWidth, moneyHeight);
        if (!wallCollisionCheck(tRect)) {
            moneySprite.translateY(velY);
        }

        // Update the player's collision rectangle for the trigger collision check
        moneyRectangle.x = moneySprite.getX();
        moneyRectangle.y = moneySprite.getY();

        // Check for collisions with non-walls and respond appropriately
        triggerCollisionCheck(moneyRectangle);

        Enemy.enemyCollisionLogic(moneyOldX, moneyOldY);

        for (Collectible coin : currentLevel.getLevelCoins()) {
            if (!(coin.isCollected()) && moneyRectangle.overlaps(coin.getCollider())) {
                coin.collect();
                money += 10;
                Score += 10;
                coin.SoundEffect.play();
            }
        }

        for (Powerup powerup : currentLevel.getLevelPowerups()) {
            if (!(powerup.isCollected()) && moneyRectangle.overlaps(powerup.getCollider())) {
                powerup.collect();
                powerup.apply(player);
                powerup.getSoundEffect().play();
            }
        }

        for (Enemy enemy : currentLevel.getLevelEnemies()) {
            if (enemy.getShowText()) {
                enemy.speechTimeCheck(delta);
            }
        }
    }

    // USE FOR NON-WALL COLLISIONS, I.E ITEMS OR ROOM TRANSITIONS
    private void triggerCollisionCheck(Rectangle pRect) {

        for (Rectangle tileRect : mapExitForwardCollisions) {
            if (pRect.overlaps(tileRect)) {
                if (currentLevel.getNextLevel() != null) {
                    switchToLevel(currentLevel.getNextLevel(),"Forward");
                }
                break;
            }
        }

        // If on LibraryFloor3 and no ExitForward layer exists, check if player is at end position
        // and automatically transition to BusLevel
        if (currentLevel.getMapName().equals("maps/libraryfloor3.tmx") && mapExitForwardCollisions.isEmpty()) {
            int endX = currentLevel.getEndX();
            int endY = currentLevel.getEndY();
            if (endX >= 0 && endY >= 0) {
                Rectangle endRect = new Rectangle(endX, endY, 1, 1);
                if (pRect.overlaps(endRect) && currentLevel.getNextLevel() != null) {
                    switchToLevel(currentLevel.getNextLevel(), "Forward");
                    return;
                }
            }
        }

        for (Rectangle tileRect : mapExitSideCollisions) {
            if (pRect.overlaps(tileRect)) {
                if (currentLevel.getSideLevel() != null) {
                    switchToLevel(currentLevel.getSideLevel(),"Side");
                }
                break;
            }
        }

        for (Rectangle tileRect : mapExitBackCollisions) {
            if (pRect.overlaps(tileRect)) {
                if (currentLevel.getPrevLevel() != null) {
                    switchToLevel(currentLevel.getPrevLevel(),"Back");
                }
                break;
            }
        }

        //added for Shop Ui to be detected when the player collides
        //with the player
        for (Rectangle tileRect : mapShopCollisions) {
            if (pRect.overlaps((tileRect))) {
                //open shop UI
                break;
            }
        }

    }

    // USED FOR MOVEMENT BASED WALL COLLISIONS ONLY
    // Checks the collision layer against the parameter rectangle,
    // returns TRUE if there is a collision, FALSE otherwise.
    private boolean wallCollisionCheck(Rectangle pRect) {

        // Iterate over every tile in our already made collision map and check if it intersects pRect
        // return TRUE if there is an overlap
        for (Rectangle tileRect : mapCollisions) {
            // If this rectangle overlaps our player's then return true.
            if (pRect.overlaps(tileRect)) {
                return true;
            }
        }
        // No collision was detected, so we can return false.
        return false;
    }

    public void logic() {
        gameTimer.tick(); // So that the timer counts down

        if (gameTimer.hasCompleted()) {
            WinOrLose = "Lose";
            gameEnded = true;
            return;
        }

        // store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // clamp x to values 0 and worldWidth -- subtract the bucketWidth
        moneySprite.setX(MathUtils.clamp(moneySprite.getX(), 0, worldWidth - moneyWidth));
        // clamp y vals
        moneySprite.setY(MathUtils.clamp(moneySprite.getY(), 0, worldHeight - moneyHeight));

        // Player rectangle constructed for collision logic in levels.
        Rectangle playerRectangle = new Rectangle(moneySprite.getX(), moneySprite.getY(), 1, 1);

        float delta = Gdx.graphics.getDeltaTime();
        // Updates the level entities.
        this.currentLevel.update(delta);
        // Collision logic for active level.
        Rectangle playerRect = new Rectangle(moneySprite.getX(), moneySprite.getY(), 1, 1);
        boolean collided = this.currentLevel.collides(playerRect);
        if (collided) {
            // If we're on the BusLevel and the player has reached the bus, switch back to the previous level
            if (this.currentLevel instanceof com.badlogic.escapefromuni.levels.BusLevel) {
                if (this.currentLevel.getPrevLevel() != null) {
                    Gdx.app.exit();
                }
            } else {
                // Generic collision handling for other levels
                System.out.println("Player collided with entity.");
            }
        }
        // apply the bucket position and size to the bucket rectangle

    }


    public void shopLogic(){
        //
    }
    //called when player collides with "ShopBlock"
    public void openShop(){
        //draw shop

        //
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        mapRenderer.render();

        stateTime += Gdx.graphics.getDeltaTime()*0.25f; // Accumulate elapsed animation time

        spriteBatch.begin();

        // Draws the level entities.
        this.currentLevel.draw(spriteBatch);

        TextureRegion currentFrame;

        if (moveDirection == "Stationary") {
            currentFrame = stationaryAnimation.getKeyFrame(stateTime, true);
        } else if (moveDirection == "Down") {
            currentFrame = downAnimation.getKeyFrame(stateTime, true);
        } else if (moveDirection == "Up") {
            currentFrame = upAnimation.getKeyFrame(stateTime, true);
        } else {
            currentFrame = rightAnimation.getKeyFrame(stateTime, true);
        }

        if (moveDirection == "Left") {
            spriteBatch.draw(currentFrame, moneySprite.getX() + moneyWidth / 2 + 1.3f, moneySprite.getY() - moneyHeight / 2 - 0.25f, -2.5f, 2.5f);
        } else {
            spriteBatch.draw(currentFrame, moneySprite.getX() - moneyWidth / 2 - 0.3f, moneySprite.getY() - moneyHeight / 2 - 0.25f, 2.5f, 2.5f);
        }
        //moneySprite.draw(spriteBatch); // Draw the character

        drawMinimap();

        for (Collectible coin : currentLevel.getLevelCoins()) {
            if (!(coin.isCollected())) {
                coin.render(spriteBatch);
            }
        }

        for (Powerup planet : currentLevel.getLevelPowerups()) {
            if (!(planet.isCollected())) {
                planet.render(spriteBatch);
            }
        }

        for (Enemy enemy : currentLevel.getLevelEnemies()) {
            if (!(enemy.isDead())) {
                enemy.render(spriteBatch);
            }
            if (enemy.getShowText()) {
                enemy.renderSpeech(spriteBatch);
            }
        }

        spriteBatch.end();

        // Draw the ui after this spritebatch as we use a separate viewport / camera
        drawUI();
    }

    private void drawUI() {
        // We use a separate ui viewport / camera as the game's resolution is too low to write any text.
        uiViewport.apply();
        spriteBatch.setProjectionMatrix(uiCamera.combined);

        spriteBatch.begin();

        // Format the time as mm:ss from the second remaining
        String tempSecs = ""+(gameTimer.getSecsRemaining()%60);
        if (tempSecs.length() == 1) {
            tempSecs = "0"+tempSecs;
        }
        String tempMins = "0"+(gameTimer.getSecsRemaining()/60);

        // Draw the formatted timer at the top center of the screen
        layout.setText(font, tempMins+":"+tempSecs);
        float tempx = (uiViewport.getWorldWidth() - layout.width) / 2f;
        font.draw(spriteBatch, layout, tempx, 900);

        // Draw the coin counter at the top center of the screen, under the timer
        layout.setText(smallFont, "Coins: "+player.getCoins());
        tempx = (uiViewport.getWorldWidth() - layout.width) / 2f;
        smallFont.draw(spriteBatch, layout, tempx, 820);

        // Draw all the event counters
        smallFont.draw(spriteBatch, "Positive Events: "+positiveEventsEncountered, 20, 950);
        smallFont.draw(spriteBatch, "Negative Events: "+negativeEventsEncountered, 20, 900);
        smallFont.draw(spriteBatch, "Hidden Events: "+hiddenEventsEncountered, 20, 850);

        spriteBatch.end();
    }

    private void drawMinimap() {
        for (int i = 0; i < levels.size(); i++){
            float h = minimapBottomHeight+i*minimapTileSize;
            levels.get(i).getMinimapSprite().setY(h);
            if (levels.get(i).getSideLevel() != null) {
                levels.get(i).getSideLevel().getMinimapSprite().setY(h);
                levels.get(i).getSideLevel().getMinimapSprite().draw(spriteBatch);
            }
            levels.get(i).getMinimapSprite().draw(spriteBatch);
        }
    }

    public void pause() {
        // Invoked when your application is paused.
    }

    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    public void dispose() {
        // Destroy application's resources here.
    }
}
