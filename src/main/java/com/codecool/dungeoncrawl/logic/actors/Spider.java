package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;


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
            int[] randomDirection = getRandomDirection();
            int dx = randomDirection[0];
            int dy = randomDirection[1];

            Cell nextCell = cell.getNeighbor(dx, dy);
            if (nextCell.getType() != CellType.WALL
                    && nextCell.getType() != CellType.CLOSED_DOOR
                    && nextCell.getType() != CellType.LAVA
                    && nextCell.getActor() == null
                    && nextCell.getItem() == null) {
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }
    }
}
