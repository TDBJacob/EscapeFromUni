package com.badlogic.escapefromuni.BusStop;

/**
 * Timer class to handle countdown functionality for the bus stop game
 */
public class GameTimer {
    private float timeRemaining;
    private float maxTime;
    private boolean isRunning;
    private boolean hasExpired;

    public GameTimer(float seconds) {
        this.maxTime = seconds;
        this.timeRemaining = seconds;
        this.isRunning = false;
        this.hasExpired = false;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public void reset() {
        timeRemaining = maxTime;
        hasExpired = false;
        isRunning = false;
    }

    public void update(float delta) {
        if (isRunning && !hasExpired) {
            timeRemaining -= delta;
            if (timeRemaining <= 0) {
                timeRemaining = 0;
                hasExpired = true;
                isRunning = false;
            }
        }
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public int getSecondsRemaining() {
        return (int) Math.ceil(timeRemaining);
    }

    public boolean isExpired() {
        return hasExpired;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public float getPercentageRemaining() {
        return timeRemaining / maxTime;
    }
}

