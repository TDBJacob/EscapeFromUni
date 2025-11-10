package com.badlogic.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class Timer
{
    private float secsRemaining;
    private boolean completed;
    private float timerSpeed;

    public Timer(int secsToWait)
    {
        this.secsRemaining = secsToWait;
        this.timerSpeed = 1f;
        this.completed = false;
    }

    public void tick()
    {
        this.secsRemaining = this.secsRemaining - Gdx.graphics.getDeltaTime()*this.timerSpeed;
        if (this.secsRemaining <= 0) {
            this.completed = true;
            this.secsRemaining = 0;
        }
    }

    public void addTime(float secs) {
        this.secsRemaining += secs;
    }

    public void removeTime(float secs) {
        this.secsRemaining -= secs;
    }

    public float getTimeRemaining()
    {
        return this.secsRemaining;
    }

    public int getSecsRemaining()
    {
        return (int) Math.floor(this.secsRemaining);
    }

    public float getTimerSpeed()
    {
        return this.timerSpeed;
    }

    public void setTimerSpeed(float nTimerSpeed)
    {
        this.timerSpeed = nTimerSpeed;
    }

    public boolean hasCompleted()
    {
        return this.completed;
    }
}
