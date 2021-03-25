package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Boat extends Item {

    public Boat(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
    }

    @Override
    public String getTileName() {
        return "boat";
    }
}
