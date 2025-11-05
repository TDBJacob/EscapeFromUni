package com.badlogic.escapefromuni.ShopStuff;

import com.badlogic.escapefromuni.Player;

public class Shop {
    //specially called off gui button press (item will be unique to the button)
    public boolean buyItem(Player player, Item item) {
        if (player.spendCoins(item.getCost())) {
            player.addItem(item);
            //add item to inventory gui
            //depending on how we want to implement the energy drink it could either be a use button or act instantly
            //need to ask during meeting
            return true;
        } else {
            //show player you dont have enough coins
            //could also just do nothing because funny
            return false;
        }
    }
}
