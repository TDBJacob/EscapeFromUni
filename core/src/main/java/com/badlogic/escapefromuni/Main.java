package com.badlogic.escapefromuni;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    final float root2 = 1.41f;

    TiledMap map; // define map
    OrthogonalTiledMapRenderer mapRenderer; // define map renderer
    FitViewport viewport;
    OrthographicCamera camera;
    Texture moneyTexture;
    Sprite moneySprite;
    Rectangle moneyRectangle;
    SpriteBatch spriteBatch;

    int objectLayerId;

    TiledMapTileLayer collisionObjectLayer;
    MapObjects objects;

    float moneyWidth;
    float moneyHeight;

    // Runs at start
    @Override
    public void create() {
        // Prepare your application here.
        viewport = new FitViewport(40, 30);
        map = new TmxMapLoader().load("practice.tmx");
        float unitScale = 1/ 16f;
        mapRenderer = new OrthogonalTiledMapRenderer(map, unitScale);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 40, 30);
        mapRenderer.setView(camera);
        moneyTexture = new Texture("vecteezy_pack-of-dollars-money-clipart-design-illustration_9391394.png");
        moneySprite = new Sprite(moneyTexture);
        moneySprite.setSize(1, 1);
        moneySprite.setX(20);
        moneyRectangle = new Rectangle();
        spriteBatch = new SpriteBatch();

        // store the bucket size for brevity
        moneyWidth = moneySprite.getWidth();
        moneyHeight = moneySprite.getHeight();

        objectLayerId = 0;

        collisionObjectLayer = (TiledMapTileLayer)map.getLayers().get(objectLayerId);
        objects = collisionObjectLayer.getObjects();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    // Runs every frame
    @Override
    public void render() {
        // Draw your application here.
        input();
        logic();
        draw();
    }

    private void input() {
        float speed = 4f; // Player's speed
        float delta = Gdx.graphics.getDeltaTime(); // Change in time between frames

        // We will use these variables to allow for consistent speed on diagonal movement.
        float velX = 0f;
        float velY = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = speed * delta; // Convert to speed/s for consistent gameplay on different FPS
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -speed * delta;
        }
        // Use if here rather than else if, so movement can happen on both axis at once
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY = speed * delta;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY = -speed * delta;
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
        if (!collisionCheck(tRect)) {
            moneySprite.translateX(velX);
        }

        tRect.set(moneySprite.getX(), moneySprite.getY()+velY, moneyWidth, moneyHeight);
        if (!collisionCheck(tRect)) {
            moneySprite.translateY(velY);
        }

    }

    // NOTE FOR COLLISIONCHECK: thinking about it, we can probably construct an array of rectangles around tiles only once to save performance
    // or something like that idk

    // Checks the collision layer against the parameter rectangle,
    // returns TRUE if there is a collision, FALSE otherwise.
    private boolean collisionCheck(Rectangle pRect) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(4); // Refers to the collision layer in Tiled

        // Iterate over every tile, construct a rectangle around the tile,
        // and check if the rectangle overlaps the player's.
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y); // Retrieve cell at our x and y

                if (cell == null) // If nothing here, skip over it
                    continue;

                // Construct rectangle at this tile position
                Rectangle tileRect = new Rectangle(x, y, 1, 1);

                // Finally, if this rectangle overlaps our player's then return true.
                if (pRect.overlaps(tileRect)) {
                    return true;
                }
            }
        }
        // No collision was detected, so we can return false.
        return false;
    }

    private void logic() {
        // store the worldWidth and worldHeight as local variables for brevity
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // clamp x to values 0 and worldWidth -- subtract the bucketWidth
        moneySprite.setX(MathUtils.clamp(moneySprite.getX(), 0, worldWidth - moneyWidth));
        // clamp y vals
        moneySprite.setY(MathUtils.clamp(moneySprite.getY(), 0, worldHeight - moneyHeight));

        float delta = Gdx.graphics.getDeltaTime();
        // apply the bucket position and size to the bucket rectangle

    }

    private void draw() {
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
        moneySprite.draw(spriteBatch); // Sprites have their own draw method

        // draw each Sprite
        //for (Sprite dropSprite : dropSprites) {
            //dropSprite.draw(spriteBatch);
        //}

        spriteBatch.end();
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
