package com.badlogic.escapefromuni.miscellaneous;

import com.badlogic.gdx.math.Rectangle;

public class Door {

    // Instantiate rectangle to cover desired section of map to block.
    Rectangle rectangle;

    public Door() {
        // instantiate rectangle to block path.
        rectangle = new Rectangle(3, 3, 3, 1);
    }

    public boolean collides(Rectangle playerRectangle) {
        return rectangle.overlaps(playerRectangle);
    }

    public void disallowCollision(Rectangle playerRectangle) {
        // disallow the collision

    }
}
