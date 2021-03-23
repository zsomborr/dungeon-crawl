package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Spider extends Actor {

    public Spider(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 5;
        strength = 8;
    }

    @Override
    public String getTileName() {
        return "spider";
    }
}
