package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 10;
        strength = 2;
        expForDeath = 1;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
