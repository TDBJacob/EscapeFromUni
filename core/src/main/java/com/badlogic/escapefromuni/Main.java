package com.badlogic.escapefromuni;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {

    Game game;

    Boolean gameStarted;

    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    @Override
    public void create() {
        gameStarted = false;

        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();
        viewport = new FitViewport(8, 5);

        //font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        //this.setScreen(new MainMenuScreen(this));

    }

    public void startGame() {
        game = new Game();
    }

    @Override
    public void resize(int width, int height) {
        if (gameStarted) {
            game.resize(width, height);
        }
    }

    // Runs every frame
    @Override
    public void render() {
        if (gameStarted) {
            game.render();
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
        game.pause();
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
        game.resume();
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
        game.dispose();
    }
}
