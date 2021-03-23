package com.codecool.dungeoncrawl.logic.item;

import com.codecool.dungeoncrawl.logic.Cell;

public class Potion extends Item {

    public Potion(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
    }

    @Override
    public String getTileName() {
        return "potion";
    }
}
