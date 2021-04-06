package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (" +
                    "saved_at, " +
                    "player_id) " +
                    "VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, state.getSavedAt());
            statement.setInt(2, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int stateId = resultSet.getInt(1);
            state.setId(stateId);

            int level = 1;
            for (String map : state.getDiscoveredMaps()) {
                int mapId = addMap(conn, map, map.equals(state.getCurrentMap()), level);
                connectMapToGameState(conn, stateId, mapId);
                level++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int addMap(Connection conn, String map, boolean isCurrentMap, int level) throws SQLException {
        String sql = "INSERT INTO map (" +
                "map_layout, " +
                "is_current_map, " +
                "game_level) " +
                "VALUES (?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, map);
        statement.setBoolean(2, isCurrentMap);
        statement.setInt(3, level);
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private void connectMapToGameState(Connection conn, int stateId, int mapId) throws SQLException{
        String sql = "INSERT INTO maps (" +
                "game_state_id, " +
                "map_id) " +
                "VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, stateId);
        statement.setInt(2, mapId);
        statement.executeUpdate();
    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int id) {
        return null;
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
