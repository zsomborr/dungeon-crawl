package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Boss;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;
    private Player player;
    private List<Actor> monsters;
    private Cell stairUp;
    private Cell stairDown;
    private Actor boss;


    public GameMap(int width, int height, CellType defaultCellType) {
        monsters = new ArrayList<>();
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public void addMonster(Actor monster) {
        monsters.add(monster);
    }

    public List<Actor> getMonsters() {
        return monsters;
    }

    public Cell getRandomEmptyCell() {
        Random random = new Random();
        while(true) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Cell randomCell = cells[x][y];
            if (randomCell.getType() == CellType.FLOOR
                    || randomCell.getType() == CellType.WALL) {
                return randomCell;
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public Cell getStairsUp() {
        return stairUp;
    }

    public Cell getStairsDown() {
        return stairDown;
    }

    public Actor getBoss() {
        return boss;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBoss(Actor boss) {
        this.boss = boss;
    }

    public void setStairDown(Cell stairDown) {
        this.stairDown = stairDown;
    }

    public void setStairUp(Cell stairUp) {
        this.stairUp = stairUp;
    }
}
