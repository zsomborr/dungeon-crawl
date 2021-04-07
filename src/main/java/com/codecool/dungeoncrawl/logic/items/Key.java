package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Key extends Item {

    public Key(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        tileName = "key";
    }

    public Key(String name) {
        this.tileName = name;
    }
}
