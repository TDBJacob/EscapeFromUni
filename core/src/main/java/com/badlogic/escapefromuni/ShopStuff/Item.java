package com.badlogic.escapefromuni.ShopStuff;

public class Item {
    private String name;
    private int cost;
    private boolean isBought;

    public Item(String name, int cost){
        this.name = name;
        this.cost = cost;
        this.isBought = false;
    }

    public String getName(){
        return this.name;
    }

    public int getCost(){
        return this.cost;
    }


    public void setisBought(boolean bool){
        this.isBought = bool;
    }
    public boolean getisBought(){
        return this.isBought;
    }



}
