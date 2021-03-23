package com.codecool.dungeoncrawl.logic.item;

import com.codecool.dungeoncrawl.logic.Cell;

public class Potion extends Item {

    public Potion(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        stat = 10;
    }

    @Override
    public String getTileName() {
        return "potion";
    }
}
