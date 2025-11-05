package com.badlogic.escapefromuni.levels;

import com.badlogic.escapefromuni.BusStop.BusStop;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Level wrapper for BusStop functionality
 */
public class BusStopLevel extends Level {
    
    private BusStop busStop;
    private ArrayList<Rectangle> pathTiles;
    
    public BusStopLevel() {
        mapName = "Bus.tmx";
        // Start player on path - middle horizontal section
        startX = 25;
        startY = 29; // On the brown path
        
        // End player at the bus stop
        endX = 21;
        endY = 0;
        
        pathTiles = new ArrayList<>();
    }

    public void setPathTiles(ArrayList<Rectangle> pathTiles) {
        this.pathTiles = pathTiles;
        // Update BusStop with path tiles for coin placement if it's initialized
        if (busStop != null) {
            busStop.setPathTiles(pathTiles);
        }
    }
    
    @Override
    public void update(float deltaTime) {
        // Update will be handled in Game's logic method with player rectangle
    }
    
    public void update(float deltaTime, Rectangle playerRectangle) {
        if (busStop != null) {
            busStop.update(deltaTime, playerRectangle);
        }
    }
    
    @Override
    public void draw(SpriteBatch batch) {
        if (busStop != null) {
            busStop.draw(batch);
        }
    }
    
    @Override
    public boolean collides(Rectangle playerRectangle) {
        // BusStop handles its own collision logic
        // We'll check this in Game's logic method
        return false;
    }
    
    public BusStop getBusStop() {
        return busStop;
    }
}

