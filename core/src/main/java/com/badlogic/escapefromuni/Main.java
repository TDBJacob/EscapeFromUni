package com.badlogic.escapefromuni;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    BitmapFont font;
    GlyphLayout layout;

    boolean paused = false;
    boolean allowPauseButton = false;

    Sprite playButtonSprite;
    Texture playButtonTexture;

    Sprite returnToMenuButtonSprite;
    Texture returnToMenuButtonTexture;

    Sprite resumeButtonSprite;
    Texture resumeButtonTexture;

    Texture menuText;
    Texture pausedText;

    int latestScore = -1;
    boolean wonLastGame;

    boolean buttonCD;

    @Override
    public void create() {
        gameStarted = false;

        buttonCD = false;
        wonLastGame = false;

        batch = new SpriteBatch();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        layout = new GlyphLayout();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 128;
        parameter.borderWidth = 1;
        parameter.borderColor = com.badlogic.gdx.graphics.Color.BLACK;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.magFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear; // smooth scaling up
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear; // smooth scaling down
        font = generator.generateFont(parameter);
        generator.dispose();

        menuText = new Texture("escapefromunititle.png");
        pausedText = new Texture("pausedtext.png");

        //this.setScreen(new MainMenuScreen(this));
        playButtonTexture = new Texture("playButton.png");
        playButtonSprite = new Sprite(playButtonTexture);
        playButtonSprite.setSize(1280/3, 200);
        playButtonSprite.setPosition(1280/3,100);

        returnToMenuButtonTexture = new Texture("returntext.png");
        returnToMenuButtonSprite = new Sprite(returnToMenuButtonTexture);
        returnToMenuButtonSprite.setSize(1280/3, 200);
        returnToMenuButtonSprite.setPosition(1280/3,80);

        resumeButtonTexture = new Texture("resumetext.png");
        resumeButtonSprite = new Sprite(resumeButtonTexture);
        resumeButtonSprite.setSize(1280/3, 200);
        resumeButtonSprite.setPosition(1280/3,330);

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

        if (!Gdx.input.isTouched()) {
            buttonCD = false;
        }

        if (gameStarted) {
            if (!game.gameEnded) {
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
                    inputPauseMenu();
                }
            } else {
                endGame(game.Score, game.WinOrLose);
            }
        } else {
            drawMainMenu();
            inputMainMenu();
        }
    }

    public void endGame(int score, String winOrLose) {
        gameStarted = false;
        paused = false;
        allowPauseButton = false;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (winOrLose != "Return") { //should be not equals
            latestScore = score;
            if (winOrLose == "Win") {
                wonLastGame = true;
            } else {
                wonLastGame = false;
            }
        } else {
            latestScore = -1;
            wonLastGame = false;
        }

        game = null;
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

            if (!buttonCD && playButtonSprite.getBoundingRectangle().contains(new Vector2(mouseX,mouseY))) {
                startGame();
                buttonCD = true;
            }
        }
    }

    private void drawMainMenu() {

        ScreenUtils.clear(Color.SKY);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        playButtonSprite.draw(batch);
        batch.draw(menuText,200,700, 880, 110);

        if (latestScore > -1) {

            if (wonLastGame) {
                layout.setText(font, "Score: " + latestScore);

                float tempx = (Gdx.graphics.getWidth() - layout.width) / 2f;
                float tempy = 480;

                font.draw(batch, layout, tempx, tempy);

                layout.setText(font, "You won!");

                tempx = (Gdx.graphics.getWidth() - layout.width) / 2f;
                tempy = 640;

                font.draw(batch, layout, tempx, tempy);
            } else {
                layout.setText(font, "You lost :(");

                float tempx = (Gdx.graphics.getWidth() - layout.width) / 2f;
                float tempy = 560;

                font.draw(batch, layout, tempx, tempy);
            }
        }

        batch.end();
    }

    private void drawPauseMenu() {

        //ScreenUtils.clear(Color.CLEAR);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.draw(pausedText,240,660, 800, 200);

        returnToMenuButtonSprite.draw(batch);
        resumeButtonSprite.draw(batch);

        batch.end();
    }

    private void inputPauseMenu() {
        if (Gdx.input.isTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = viewport.getScreenHeight()-Gdx.input.getY();


            //Gdx.app.log("MyTag", mouseX + " " + mouseY);
            //Gdx.app.log("buttonxy", playButtonSprite.getBoundingRectangle().x+" "+playButtonSprite.getBoundingRectangle().y);

            if (!buttonCD) {
                if (resumeButtonSprite.getBoundingRectangle().contains(new Vector2(mouseX, mouseY))) {
                    paused = false;
                    ScreenUtils.clear(Color.CLEAR);
                    game.draw();
                } else if (returnToMenuButtonSprite.getBoundingRectangle().contains(new Vector2(mouseX, mouseY))) {
                    endGame(game.Score, game.WinOrLose);
                    buttonCD = true;
                }
            }
        }
    }

}
