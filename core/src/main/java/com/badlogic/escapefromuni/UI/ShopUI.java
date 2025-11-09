package com.badlogic.escapefromuni.UI;

import com.badlogic.escapefromuni.Player;
import com.badlogic.escapefromuni.ShopStuff.Item;
import com.badlogic.escapefromuni.ShopStuff.Shop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * ShopUi is used to draw all sprites needed for the Shop UI.
 *
 * drawShopMenu() is called everytime you are within the confines of a ShopBlock
 * collision rectangle
 *
 * inputShopMenu is used to detect if the buttons are being clicked
 * and calls the respective functions for what item is being bought
 *
 * energy drink gives 30 secs of 1.1x speed
 * */
public class ShopUI {

    Mouse mouse = new Mouse();
    Shop shop = new Shop();
    //make the two items that can be bought in the shop
    Item energyDrink = new Item("energyDrink", 5);
    Item birdFeed = new Item("birdFeed", 5);

    public void drawShopMenu(Viewport viewport, SpriteBatch batch, Sprite shopTestSprite,Sprite buyEDSprite,Sprite buyBFSprite,BitmapFont shopfont, GlyphLayout layout) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        shopTestSprite.draw(batch);
        buyEDSprite.draw(batch);
        buyBFSprite.draw(batch);

        layout.setText(shopfont, "Buy Energy Drink: 5 coins");

        float tempx = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float tempy = 650;

        shopfont.draw(batch, layout, tempx, tempy);

        layout.setText(shopfont, "Buy Bird Feed: 5 coins");

        tempx = (Gdx.graphics.getWidth() - layout.width) / 2f;
        tempy = 450;

        shopfont.draw(batch, layout, tempx, tempy);

        batch.end();
    }

    public void inputShopMenu(Viewport viewport, Sprite buyEDSprite, Sprite buyBFSprite, boolean buttonCD, Player player) {
        if (Gdx.input.isTouched()) {
            mouse.update(viewport);
            if (!buttonCD) {

                if (buyEDSprite.getBoundingRectangle().contains(new Vector2(mouse.getX(), mouse.getY()))) {

                    shop.buyItem(player, energyDrink);
                }
                if (buyBFSprite.getBoundingRectangle().contains(new Vector2(mouse.getX(), mouse.getY()))) {
                    shop.buyItem(player, birdFeed);
                }
            }

        }
    }

}
