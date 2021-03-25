package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Direction;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super.cell = cell;
        super.cell.setActor(this);
        health = 10;
        strength = 2;
        expForDeath = 1;
    }

    @Override
    public void move() {
        if (health > 0) {
            boolean haveMoved = false;
            int tries = 0;
            while (!haveMoved) {
                try {
                    Cell playerPos = cell.getGameMap().getPlayer().getCell();
                    int[] direction;
                    if (playerPos.getY() > cell.getY() && tries == 0) {
                        direction = Direction.DOWN.getDirection();
                    } else if (playerPos.getY() < cell.getY() && tries == 1) {
                        direction = Direction.UP.getDirection();
                    } else if (playerPos.getX() > cell.getX() && tries == 2) {
                        direction = Direction.RIGHT.getDirection();
                    } else if (playerPos.getX() < cell.getX() && tries == 3) {
                        direction = Direction.LEFT.getDirection();
                    } else {
                        direction = Direction.getRandomDirection();
                    }
                    int dx = direction[0];
                    int dy = direction[1];
                    super.move(dx, dy);
                    haveMoved = true;
                    tries = 0;
                } catch (Exception e) {
                    System.out.println("ige");
                    haveMoved = false;
                    tries++;
                }
            }
        }
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
