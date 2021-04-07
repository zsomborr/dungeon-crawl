package com.codecool.dungeoncrawl.model;

import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private String saveName;
    private Date savedAt;
    private int playerId;

    public GameState(Date savedAt, int playerId, String saveName) {
        this.saveName = saveName;
        this.savedAt = savedAt;
        this.playerId = playerId;
    }

    public String getSaveName() {
        return saveName;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayer(PlayerModel player) {
        this.playerId = playerId;
    }
}
