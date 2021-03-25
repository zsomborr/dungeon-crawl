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
        for (Cell[] cellRow : cells) {
            for (Cell cellCol : cellRow) {
                if (cellCol.getType() == CellType.STAIRS_UP){
                    return cellCol;
                }
            }
        }
        return null;
    }

    public Cell getStairsDown() {
        for (Cell[] cellRow : cells) {
            for (Cell cellCol : cellRow) {
                if (cellCol.getType() == CellType.STAIRS_DOWN){
                    return cellCol;
                }
            }
        }
        return null;
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
}
