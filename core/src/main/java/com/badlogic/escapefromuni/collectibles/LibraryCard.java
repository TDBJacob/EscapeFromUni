package com.badlogic.escapefromuni.collectibles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Class LibraryCard represents the library card, it has collision, texture, sprite, a list of spawns.
 */
public class LibraryCard {

    Texture texture = new Texture("blahblah.png");
    Sprite sprite = new Sprite(texture);
    List<List<Integer>> spawns = this.populateSpawns();
    Rectangle rectangle;
    List<Integer> spawn;

    public LibraryCard() {
        // logic for assigning spawn randomly and creatign rectangle
    }

    // Populates the spawns for the library card.
    private List<List<Integer>> populateSpawns() {
        List<List<Integer>> spawns = new ArrayList<>();
        List<Integer> spawn1 = List.of(31, 5);
        List<Integer> spawn2 = List.of(37, 22);
        List<Integer> spawn3 = List.of(24, 9);
        List<Integer> spawn4 = List.of(11, 3);
        spawns.add(spawn1);
        spawns.add(spawn2);
        spawns.add(spawn3);
        spawns.add(spawn4);
        return spawns;
    }

    // collision logic between player and library card.
}
