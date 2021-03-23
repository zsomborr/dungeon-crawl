package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Ghost extends Actor {
    private int teleportCoolDown;

    public Ghost(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 20;
        strength = 3;
        teleportCoolDown = 0;
    }

    @Override
    public String getTileName() {
        return "ghost";
    }

    @Override
    public void move() {
        if (health > 0) {
            Cell nextCell;
            if (teleportCoolDown == 4) {
                nextCell = cell.getGameMap().getRandomEmptyCell();
                teleportCoolDown = 0;
            } else {
                int[] randomDirection = getRandomDirection();
                int dx = randomDirection[0];
                int dy = randomDirection[1];
                nextCell = cell.getNeighbor(dx, dy);
                teleportCoolDown++;
            }
            if (nextCell.getType() != CellType.EMPTY
                    && nextCell.getType() != CellType.CLOSED_DOOR
                    && nextCell.getActor() == null
                    && nextCell.getItem() == null) {
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }
    }
}

