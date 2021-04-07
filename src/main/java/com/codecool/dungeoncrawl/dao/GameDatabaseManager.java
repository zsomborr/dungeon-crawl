package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapWriter;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.util.Util;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private MapDao mapDao;
    private PlayerModel playerModel;
    private GameState gameState;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        mapDao = new MapDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) {
        this.playerModel = new PlayerModel(player);
        playerDao.add(playerModel);
    }

    public void updatePlayer(Player player, int playerId) {
        PlayerModel newPlayerModel = new PlayerModel(player);
        newPlayerModel.setId(playerId);
        playerDao.update(newPlayerModel);
    }

    public void saveGameState(String saveName) {
        int playerId = playerModel.getId();
        Date savedAt = Util.getCurrentDateAndTime();
        this.gameState = new GameState(savedAt, playerId, saveName);
        gameStateDao.add(gameState);
    }

    public void updateGameState(int gameStateId) {
        GameState oldGameState = gameStateDao.get(gameStateId);
        Date savedAt = Util.getCurrentDateAndTime();
        oldGameState.setSavedAt(savedAt);
        gameStateDao.update(oldGameState);
    }

    public void saveMaps(List<GameMap> maps, GameMap currentMap) {
        int gameLevel = 1;
        int stateId = gameState.getId();

        for (GameMap map : maps) {
            String mapTxt = MapWriter.getMapTxt(map);
            MapModel mapModel;
            if (map.equals(currentMap)) {
                mapModel = new MapModel(stateId, mapTxt, true, gameLevel);
            } else {
                mapModel = new MapModel(stateId, mapTxt, false, gameLevel);
            }
            mapDao.add(mapModel, stateId);
            gameLevel++;
        }
    }

    public void updateMaps(ArrayList<GameMap> maps, GameMap currentMap, int oldGameStateId) {
        List<MapModel> oldMaps = mapDao.getAll(oldGameStateId);

        for (int i = 0; i < oldMaps.size(); i++) {
            oldMaps.get(i).setMapLayout(MapWriter.getMapTxt(maps.get(i)));
            oldMaps.get(i).setCurrentMap(maps.get(i).equals(currentMap));

            mapDao.update(oldMaps.get(i), oldGameStateId);
        }
    }

    public GameState checkIfSaveNameExists(String saveName) {
        List<GameState> gameStates = gameStateDao.getAll();

        for (GameState state : gameStates) {
            if (state.getSaveName().equals(saveName)) {
                return state.getId();
            }
        }
        return -1;
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
