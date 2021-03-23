package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Actor implements Drawable {
    protected Cell cell;
    protected int health;
    protected int strength;

    public void move(int dx, int dy) {
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

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public int[] getRandomDirection() {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{0, -1});
        directions.add(new int[]{0, 1});
        directions.add(new int[]{-1, 0});
        directions.add(new int[]{1, 0});

        Random random = new Random();
        return directions.get(random.nextInt(4));
    }
}
