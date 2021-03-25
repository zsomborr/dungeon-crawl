package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    protected Cell cell;
    protected int health;
    protected int strength;
    protected int expForDeath;

    public void move(int dx, int dy) throws Exception{
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() != CellType.WALL
                && nextCell.getType() != CellType.CLOSED_DOOR
                && nextCell.getType() != CellType.LAVA
                && nextCell.getActor() == null
                && nextCell.getItem() == null) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else throw new Exception();
    }

    public void move() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public int getExpForDeath() {
        return expForDeath;
    }
}
