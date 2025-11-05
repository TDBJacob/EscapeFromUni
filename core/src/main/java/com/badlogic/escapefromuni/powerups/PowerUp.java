package com.badlogic.escapefromuni.powerups;

import com.badlogic.escapefromuni.Player;

public interface PowerUp {
    public void apply(Player player); //called when used


    void remove(Player player); //called when the power up ends (if we want it timed)



    boolean isTemp();


    boolean update(Player player, float deltaTime);

}
