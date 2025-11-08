package com.badlogic.escapefromuni;

import com.badlogic.escapefromuni.levels.*;
import com.badlogic.escapefromuni.Player;
import com.badlogic.escapefromuni.powerups.EnegryDrinkPowerUp;
import com.badlogic.escapefromuni.powerups.PowerUp;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;

public class Game {

    public boolean gameEnded;
    public int Score;
    public String WinOrLose;

    final float root2 = 1.41f;

    float minimapTileSize = 1.4f;

    Player player = new Player(16f);//set default speed

    ArrayList<Level> levels;

    TiledMap map; // define map
    OrthogonalTiledMapRenderer mapRenderer; // define map renderer
    FitViewport viewport;
    OrthographicCamera camera;
    Texture moneyTexture;
    Sprite moneySprite;
    Rectangle moneyRectangle;
    SpriteBatch spriteBatch;

    Level currentLevel;

    float unitScale;

    TiledMapTileLayer collisionObjectLayer;
    MapObjects objects;

    float moneyWidth;
    float moneyHeight;

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

    //used by will for side level
    //Level level2;

    ArrayList<Sprite> minimapSprites;

    // Runs at start
    public Game() {

        WinOrLose = "Return"; // Should be "Return"
        gameEnded = false;
        Score = 0;

        // IMPORTANT: This is the list of levels, the player can traverse back and forth in this order.
        //            Add appropriate exits forward and/or backward in the tilemap on their individual layers.
        levels = new ArrayList<Level>(Arrays.asList(new LibraryFloor3(), new LibraryFloor2(), new LibraryFloor1(), new LibraryFloor0(), new BusLevel()));

        emptyMinimapIcon = new Texture("emptyminimap.png");
        playerMinimapIcon = new Texture("occupiedminimap.png");

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
        levels.get(1).setSideLevel(ShopLevel);
        ShopLevel.setSideLevel(levels.get(1));

        ShopLevel.setMinimapSprite(new Sprite(emptyMinimapIcon));
        ShopLevel.getMinimapSprite().setX(38f-minimapTileSize);
        ShopLevel.getMinimapSprite().setSize(minimapTileSize-0.1f,minimapTileSize-0.1f);




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
        moneySprite.setSize(1, 1);
        moneySprite.setX(40);
        moneySprite.setY(27);
        moneyRectangle = new Rectangle();
        spriteBatch = new SpriteBatch();

        switchToLevel(currentLevel, "Forward");

        // store the bucket size for brevity
        moneyWidth = moneySprite.getWidth();
        moneyHeight = moneySprite.getHeight();

        moneyRectangle.setSize(1,1);
        moneyRectangle.x = moneySprite.getX();
        moneyRectangle.y = moneySprite.getY();

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
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    //render is called in main

    public void input() {
        //float speed = 16f; // Player's speed
        float delta = Gdx.graphics.getDeltaTime(); // Change in time between frames
        player.update(delta); //called to check for active powerups

        // We will use these variables to allow for consistent speed on diagonal movement.
        float velX = 0f;
        float velY = 0f;

        //experiment of power up
        //a key press (P) will be used to give the power up instead of a gui button for now
        //higher than 1.5 X speed crashes the collision logic and breaks the game
        if (Gdx.input.isKeyPressed((Input.Keys.E)) ){
            PowerUp drinkPowerUp = new EnegryDrinkPowerUp(1.1f, 5.0f);
            player.addPowerUp(drinkPowerUp);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = player.getSpeed() * delta; // Convert to speed/s for consistent gameplay on different FPS
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -player.getSpeed() * delta;
        }
        // Use if here rather than else if, so movement can happen on both axis at once
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY = player.getSpeed() * delta;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY = -player.getSpeed() * delta;
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
        //spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // store the worldWidth and worldHeight as local variables for brevity
        //float worldWidth = viewport.getWorldWidth();
        //float worldHeight = viewport.getWorldHeight();

        //spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
        //spriteBatch.draw(knightTexture, 0, 0, 1, 1); // draw the bucket -- made obsolete by use of Sprite

        // Draws the level entities.
        this.currentLevel.draw(spriteBatch);

        moneySprite.draw(spriteBatch); // Sprites have their own draw method

        // draw each Sprite
        //for (Sprite dropSprite : dropSprites) {
        //dropSprite.draw(spriteBatch);
        //}

        drawMinimap();

        spriteBatch.end();
    }

    private void drawMinimap() {
        for (int i = 0; i < levels.size(); i++){
            float h = +24+i*minimapTileSize;
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
