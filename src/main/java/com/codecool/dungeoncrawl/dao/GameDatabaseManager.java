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
import java.util.List;
import java.util.stream.Collectors;

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

    public void saveGameState(String saveName) {
        int playerId = playerModel.getId();
        Date savedAt = Util.getCurrentDateAndTime();
        this.gameState = new GameState(savedAt, playerId, saveName);
        gameStateDao.add(gameState);
    }

    public void saveMaps(List<GameMap> maps, GameMap currentMap) {
        int gameLevel = 1;
        int stateId = gameState.getId();
        for (GameMap map : maps) {
            String mapTxt = MapWriter.getMapTxt(map);
            MapModel mapModel;
            if (map.equals(currentMap)) {
                mapModel = new MapModel(mapTxt, true, gameLevel);
            } else {
                mapModel = new MapModel(mapTxt, false, gameLevel);
            }
            mapDao.add(mapModel, stateId);
            gameLevel++;
        }
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
