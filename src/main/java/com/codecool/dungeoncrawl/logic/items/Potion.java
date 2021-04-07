package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Potion extends Item {

    public Potion(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        stat = 5;
        tileName = "potion";
    }

    public Potion(String name) {
        this.tileName = name;
    }
}
