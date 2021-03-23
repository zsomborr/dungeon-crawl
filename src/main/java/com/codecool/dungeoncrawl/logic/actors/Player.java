package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.item.Item;
import com.codecool.dungeoncrawl.logic.item.Potion;
import com.codecool.dungeoncrawl.logic.item.Sword;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {
    private boolean onItem = false;
    private List<Item> inventory;

    public Player(Cell cell) {
        health = 20;
        strength = 5;
        inventory = new ArrayList<>();
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
            checkIfOnItem(nextCell);
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else if (nextCell.getActor() != null) {
            fight(nextCell);
            System.out.println(health);
        }
    }
}
