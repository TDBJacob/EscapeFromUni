package com.badlogic.escapefromuni.ShopStuff;

import com.badlogic.escapefromuni.entities.Player;
import com.badlogic.escapefromuni.powerups.Powerup;
import com.badlogic.escapefromuni.powerups.SpeedPowerup;

public class Shop {
    //specially called off gui button press (item will be unique to the button)
    public boolean buyItem(Player player, Item item) {
        if (!item.getisBought() && player.spendCoins(item.getCost())){

                player.addItem(item);
                item.setisBought(true);
                System.out.println(item.getName());
                if (item.getName() == "energyDrink"){
                    SpeedPowerup drinkPowerUp = new SpeedPowerup(null, null, 0, 0, 1.1f, 30.0f);
                    player.addSpeedPowerUp(drinkPowerUp);
                }
                if (item.getName() == "birdFeed"){
                    player.setHasBirdFeed();
                }
                return true;
        }
        else {
            //show player you dont have enough coins
            //could also just do nothing because funny
            return false;
        }
    }
}
