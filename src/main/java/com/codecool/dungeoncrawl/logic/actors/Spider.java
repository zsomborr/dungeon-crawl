package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;


public class Spider extends Actor {

    public Spider(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 5;
        strength = 6;
        expForDeath = 1;
    }

    @Override
    public String getTileName() {
        return "spider";
    }


    @Override
    public void move() {
        if (health > 0) {
            try {
                int[] randomDirection = Direction.getRandomDirection();
                int dx = randomDirection[0];
                int dy = randomDirection[1];
                super.move(dx, dy);
            } catch (Exception ignore){
            }
        }
    }
}
