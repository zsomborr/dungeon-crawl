package com.codecool.dungeoncrawl.model;

public class MapModel extends BaseModel{

    private int gameStateId;
    private String mapLayout;
    private boolean currentMap;
    private int gameLevel;

    public MapModel(int gameStateId, String mapLayout, boolean currentMap, int gameLevel) {
        this.mapLayout = mapLayout;
        this.currentMap = currentMap;
        this.gameLevel = gameLevel;
        this.gameStateId = gameStateId;
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

    public void setMapLayout(String mapLayout) {
        this.mapLayout = mapLayout;
    }

    public void setCurrentMap(boolean currentMap) {
        this.currentMap = currentMap;
    }
}
