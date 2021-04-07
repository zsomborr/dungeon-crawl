package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {

    protected Cell cell;
    protected int stat;
    protected String tileName;

    public int getStat() {
        return stat;
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

    public String getTileName() {return tileName;}

    @Override
    public String toString() {
        return this.getTileName();
    }
}
