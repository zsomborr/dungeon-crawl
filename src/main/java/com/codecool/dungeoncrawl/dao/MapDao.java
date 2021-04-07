package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;

import java.util.List;

public interface MapDao {
    void add(MapModel map, int stateId);
    void update(MapModel map, int stateId);
    MapModel get(int id);
    List<MapModel> getAll(int stateId);
}
