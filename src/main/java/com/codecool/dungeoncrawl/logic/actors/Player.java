package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.*;

import java.util.*;

public class Player extends Actor {
    private int experience;
    private String name;
    private boolean onItem = false;
    private List<Item> inventory;
    private boolean onStairsDown;
    private boolean onStairsUp;
    private int poisonCount;

    public Player(Cell cell, String name) {
        health = 30;
        strength = 5;
        inventory = new ArrayList<>();
        super.cell = cell;
        super.cell.setActor(this);
        this.name = name;
    }

    public Player(Cell cell) {
        health = 30;
        strength = 5;
        inventory = new ArrayList<>();
        super.cell = cell;
        super.cell.setActor(this);
    }

    public int getPoisonCount() {
        return poisonCount;
    }

    public int getExperience() {
        return experience;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getTileName() {
        return "player";
    }

    public boolean isOnItem() {
        return onItem;
    }

    public boolean isOnStairsUp() {
        return onStairsUp;
    }

    public boolean isOnStairsDown() {
        return onStairsDown;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setPoisonCount(int poisonCount) {
        this.poisonCount = poisonCount;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public void addToInventory() {
        Item item = super.cell.getItem();
        changePlayerStat(item);
        inventory.add(item);
        this.cell.setItem(null);
    }

    private void changePlayerStat(Item item) {
        if (item instanceof Potion) {
            health += item.getStat();
        } else if (item instanceof Sword) {
            strength += item.getStat();
        }
    }

    public String getInventoryString() {
        StringBuilder inventoryFormatted = new StringBuilder();
        ArrayList<String> inventoryString = inventoryToStrings();
        Set<String> inventorySet = new HashSet<>(inventoryString);
        for (String item : inventorySet) {
            inventoryFormatted.append(item)
                    .append(" ")
                    .append(Collections.frequency(inventoryString, item))
                    .append("\n");
        }
        return inventoryFormatted.toString();
    }

    public ArrayList<String> inventoryToStrings() {
        ArrayList<String> inventoryString = new ArrayList<>();
        for (Item item : inventory) {
            inventoryString.add(item.getTileName());
        }
        return inventoryString;
    }

    public String[] inventoryToStringArray() {
        String[] inventoryString = new String[inventory.size()];
        for (int i = 0; i < inventory.size(); i++) {
            inventoryString[i] = inventory.get(i).getTileName();
        }
        return inventoryString;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        onStairsUp = false;
        onStairsDown = false;
        checkIfPoisoned();
        Cell nextCell = cell.getNeighbor(dx, dy);

        if (nextCell.getType() != CellType.CLOSED_DOOR
                && nextCell.getActor() == null) {
            handleCollision(nextCell);
        } else if (nextCell.getType() == CellType.CLOSED_DOOR) {
            tryOpenDoor(nextCell);
        } else if (nextCell.getType() == CellType.STAIRS_UP) {
            onStairsUp = true;
        } else if (nextCell.getType() == CellType.STAIRS_DOWN) {
            onStairsDown = true;
        } else if (nextCell.getActor() != null && nextCell.getActor() != this) {
            fight(nextCell);
        }
    }

    private void handleCollision(Cell nextCell){
        if (name.equals("zsombor") || name.equals("bence")) {
            takeStep(nextCell);
        } else {
            if (nextCell.getType() == CellType.LAVA) {
                health -= 10;
                takeStep(nextCell);
            } else if (nextCell.getType() == CellType.WATER) {
                if (hasBoat()) {
                    takeStep(nextCell);
                }
            } else if (nextCell.getType() != CellType.WALL){
                takeStep(nextCell);
            }
        }
    }

    private void takeStep(Cell nextCell) {
        checkIfOnItem(nextCell);
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    private void tryOpenDoor(Cell nextCell) {
        Item key = null;
        for (Item item : inventory) {
            if (item instanceof Key) {
                nextCell.setType(CellType.OPEN_DOOR);
                key = item;
            }
        }
        inventory.remove(key);
    }

    private boolean hasBoat() {
        for (Item item : inventory) {
            if (item instanceof Boat) {
                return true;
            }
        }
        return false;
    }

    private void fight(Cell nextCell) {
        Actor enemy = nextCell.getActor();
        if (enemy instanceof Spider) {
            poisonCount = 3;
        }
        int enemyHealth = enemy.getHealth();
        int enemyStrength = enemy.getStrength();
        enemy.setHealth(enemyHealth - strength);
        this.setHealth(health - enemyStrength);
        if (enemy.getHealth() <= 0) {
            int plusExp = enemy.getExpForDeath();
            experience += plusExp;
            this.setHealth(health + (plusExp * 3));
            this.setStrength(strength + plusExp);
            if (!(enemy instanceof Boss)) {
                nextCell.setActor(null);
            }
        }
    }

    private void checkIfPoisoned() {
        if (poisonCount > 0) {
            health--;
            poisonCount--;
        }
    }

    public boolean isPoisoned() {
        return poisonCount > 0;
    }

    private void checkIfOnItem(Cell nextCell) {
        if (nextCell.getItem() != null) {
            onItem = true;
        }
    }

    public List<Item> getInventory() {
        return inventory;
    }
}
