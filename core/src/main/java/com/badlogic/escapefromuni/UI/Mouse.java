package com.badlogic.escapefromuni.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

/**The mouse class is used to get the coordinates of your mouse
 * (the y is upside down by default in libgdx so it is flipped)
 */

public class Mouse {
    private float x;
    private float y;
    Viewport viewport;

    /**
     * updates the x and y coordinates to the current mouse position
     * @param viewport used to flip the y coordinate
     */
    public void update(Viewport viewport) {
        x = Gdx.input.getX();
        y = viewport.getScreenHeight()-Gdx.input.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
