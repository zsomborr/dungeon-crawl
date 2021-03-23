package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.item.Item;
import com.codecool.dungeoncrawl.logic.item.Key;
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

    public void addToInventory() {
        Item item = super.cell.getItem();
        changePlayerStat(item);
        inventory.add(item);
        this.cell.setItem(null);
    }

    private void changePlayerStat(Item item) {
        if (item instanceof Potion){
            health += item.getStat();
        } else if (item instanceof Sword) {
            strength += item.getStat();
        }
    }

    public String getTileName() {
        return "player";
    }

    public boolean isOnItem() {
        return onItem;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() != CellType.WALL && nextCell.getType() != CellType.CLOSED_DOOR
                && nextCell.getActor() == null) {
            checkIfOnItem(nextCell);
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        } else if (nextCell.getActor() != null) {
            fight(nextCell);
            System.out.println(health);
        } else if (nextCell.getType() == CellType.CLOSED_DOOR) {
            tryOpenDoor(nextCell);
        }
    }

    private void tryOpenDoor(Cell nextCell) {
        for (Item item : inventory) {
            if (item instanceof Key){
                nextCell.setType(CellType.OPEN_DOOR);
            }
        }
    }

    private void fight(Cell nextCell) {
        int enemyHealth = nextCell.getActor().getHealth();
        int enemyStrength = nextCell.getActor().getStrength();
        nextCell.getActor().setHealth(enemyHealth - strength);
        this.setHealth(health - enemyStrength);
        if (nextCell.getActor().getHealth() <= 0) {
            nextCell.setActor(null);
        }
        System.out.println(health);
    }

    private void checkIfOnItem(Cell nextCell) {
        if (nextCell.getItem() != null) {
            onItem = true;
        }
    }
}
