package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    LAVA("lava"),
    CLOSED_DOOR("closed door"),
    OPEN_DOOR("open door"),
    STAIRS_UP("stairsUp"),
    STAIRS_DOWN("stairsDown"),
    WATER("water"),
    BRIDGE("bridge");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
