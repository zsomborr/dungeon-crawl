package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Boss extends Actor {

    public Boss(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 60;
        strength = 10;
        expForDeath = 20;
    }

    @Override
    public String getTileName() {
        return "boss";
    }
}
