package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Player extends Actor {
    private boolean onItem = false;

    public Player(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
    }

    public String getTileName() {
        return "player";
    }

    public boolean isOnItem() {
        return onItem;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() != CellType.WALL && nextCell.getActor() == null) {
            if (nextCell.getItem() != null) {
                onItem = true;
            }
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }
}
