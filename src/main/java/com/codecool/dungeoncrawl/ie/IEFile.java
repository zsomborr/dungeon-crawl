package com.codecool.dungeoncrawl.ie;

import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.Date;
import java.util.List;

public class IEFile {

    private Date savedAt;
    private PlayerModel playerModel;
    private List<MapModel> mapModels;

    public IEFile(Date savedAt, PlayerModel player, List<MapModel> mapModels) {
        this.savedAt = savedAt;
        this.playerModel = player;
        this.mapModels = mapModels;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public PlayerModel getPlayer() {
        return playerModel;
    }

    public void setPlayer(PlayerModel player) {
        this.playerModel = player;
    }

    public List<MapModel> getMapModels() {
        return mapModels;
    }

    public void setMapModels(List<MapModel> mapModels) {
        this.mapModels = mapModels;
    }
}
