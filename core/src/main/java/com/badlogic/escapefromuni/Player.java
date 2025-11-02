package com.badlogic.escapefromuni;

import com.badlogic.escapefromuni.powerups.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private float speed;
    public final float defaultSpeed = 16f; //game start with this speed
    private List<PowerUp> activePowerUps = new ArrayList<>(); //this will be the list containing all the active powerups
    public Player(float speed){
        this.speed = speed;
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUp.apply(this);
        activePowerUps.add(powerUp);
    }


    public void increaseSpeed(float multiplier){
        this.speed *= multiplier;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public float getSpeed(){
        return this.speed;
    }

    public void update(float deltaTime) {
        // Movement, game logic, etc.
        Iterator<PowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp p = iterator.next();
            if (p.update(this, deltaTime)) {
                iterator.remove();
            }
        }
    }


}
