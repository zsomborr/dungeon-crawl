package com.codecool.dungeoncrawl.logic.items;


import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Item {

    public Sword(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        stat = 2;
        this.tileName = "sword";
    }

    public Sword(String name){
        tileName = name;
    }
}
