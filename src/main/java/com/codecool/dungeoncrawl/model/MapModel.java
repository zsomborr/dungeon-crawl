package com.codecool.dungeoncrawl.model;

public class MapModel {

    private int gameStateId;
    private String mapLayout;
    private boolean currentMap;
    private int gameLevel;

    public MapModel(String mapLayout, boolean currentMap, int gameLevel) {
        this.mapLayout = mapLayout;
        this.currentMap = currentMap;
        this.gameLevel = gameLevel;
    }

    public String getMapLayout() {
        return mapLayout;
    }

    public boolean isCurrentMap() {
        return currentMap;
    }

    public int getGameLevel() {
        return gameLevel;
    }
}
