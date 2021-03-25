package com.codecool.dungeoncrawl.logic;

import java.util.List;
import java.util.Random;

public enum Direction {
    UP(new int[]{0, -1}),
    DOWN(new int[]{0, 1}),
    LEFT(new int[]{-1, 0}),
    RIGHT(new int[]{1, 0});

    private final int[] direction;

    private static final List<Direction> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static int[] getRandomDirection()  {
        return VALUES.get(RANDOM.nextInt(SIZE)).direction;
    }

    public int[] getDirection() {
        return direction;
    }

    Direction(int[] direction) {
        this.direction = direction;
    }
}
