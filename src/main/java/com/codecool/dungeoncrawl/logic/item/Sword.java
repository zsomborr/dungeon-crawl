package com.codecool.dungeoncrawl.logic.item;


import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Item {

    public Sword(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        stat = 2;
    }

    @Override
    public String getTileName() {
        return "sword";
    }
}
