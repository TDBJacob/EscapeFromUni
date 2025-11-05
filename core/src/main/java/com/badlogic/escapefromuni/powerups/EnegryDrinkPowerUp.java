package com.badlogic.escapefromuni.powerups;

import com.badlogic.escapefromuni.Player;

public class EnegryDrinkPowerUp implements PowerUp {
    private float speedMult;
    private float duration; //seconds
    private float timeActive;

    public EnegryDrinkPowerUp(float speedMult, float duration){
        this.speedMult = speedMult;
        this.duration = duration;
        this.timeActive = 0.0f;
    }

    //if changing speed then remember it is a multiplicative change rather then linear
    @Override
    public void apply(Player player) {
        player.increaseSpeed(speedMult);
    }

    @Override
    public void remove(Player player) {
        player.setSpeed(player.defaultSpeed);
    }

    @Override
    public boolean isTemp() {
        return true;
    }

    // Call this each frame
    public boolean update(Player player, float deltaTime) {
        if (!isTemp()) return false;

        timeActive += deltaTime;
        if (timeActive >= duration) {
            remove(player);
            return true; // expired
        }
        return false;
    }
}
