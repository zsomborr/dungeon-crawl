package com.codecool.dungeoncrawl.logic.item;

import com.codecool.dungeoncrawl.logic.Cell;

public class Key extends Item {

    public Key(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
    }

    @Override
    public String getTileName() {
        return "key";
    }
}
