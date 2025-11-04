package com.badlogic.escapefromuni;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    Game game;

    Boolean gameStarted;

    public SpriteBatch batch;
    public FitViewport viewport;

    boolean paused = false;
    boolean allowPauseButton = false;

    Sprite playButtonSprite;
    Texture playButtonTexture;

    Texture menuText;
    Texture pausedText;

    @Override
    public void create() {
        gameStarted = false;

        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        menuText = new Texture("escapefromunititle.png");
        pausedText = new Texture("pausedtext.png");

        //this.setScreen(new MainMenuScreen(this));
        playButtonTexture = new Texture("Blue_rectangle.png");
        playButtonSprite = new Sprite(playButtonTexture);
        playButtonSprite.setSize(1280/3, 200);
        playButtonSprite.setPosition(1280/3,100);

        //startGame();
    }

    public void startGame() {
        game = new Game();

        gameStarted = true;
        paused = false;
        allowPauseButton = true;
        game.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        if (gameStarted) {
            game.resize(width, height);
        } else {
            viewport.update(width, height, true);
        }
    }

    // Runs every frame
    @Override
    public void render() {
        if (gameStarted) {

            if(allowPauseButton && Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                //allowPauseButton = false;
                paused = !paused;
                ScreenUtils.clear(Color.CLEAR);
            }

            game.draw();

            if (!paused) {
                game.input();
                game.logic();
            } else {
                drawPauseMenu();
            }

        } else {
            drawMainMenu();
            inputMainMenu();
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
        //game.pause();
        paused = true;
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        //game.resume();
        paused = false;
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
        //game.dispose();
    }

    private void inputMainMenu() {
        if (Gdx.input.isTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = viewport.getScreenHeight()-Gdx.input.getY();


            //Gdx.app.log("MyTag", mouseX + " " + mouseY);
            //Gdx.app.log("buttonxy", playButtonSprite.getBoundingRectangle().x+" "+playButtonSprite.getBoundingRectangle().y);

            if (playButtonSprite.getBoundingRectangle().contains(new Vector2(mouseX,mouseY))) {
                startGame();
            }
        }
    }

    private void drawMainMenu() {

        ScreenUtils.clear(Color.SKY);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        playButtonSprite.draw(batch);
        batch.draw(menuText,200,600, 880, 110);

        batch.end();
    }

    private void drawPauseMenu() {

        //ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(pausedText,240,350, 800, 300);

        batch.end();
    }

}
