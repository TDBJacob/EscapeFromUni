package com.badlogic.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class Timer
{
    private float secsRemaining;
    private boolean completed;

    public Timer(int secsToWait)
    {
        this.secsRemaining = secsToWait;
        this.completed = false;
    }

    public void tick()
    {
        this.secsRemaining = this.secsRemaining - Gdx.graphics.getDeltaTime();
        if (this.secsRemaining <= 0) {
            this.completed = true;
            this.secsRemaining = 0;
        }
    }

    public float getTimeRemaining()
    {
        return this.secsRemaining;
    }

    public int getSecsRemaining()
    {
        return (int) Math.floor(this.secsRemaining);
    }

    public boolean hasCompleted()
    {
        return this.completed;
    }
}