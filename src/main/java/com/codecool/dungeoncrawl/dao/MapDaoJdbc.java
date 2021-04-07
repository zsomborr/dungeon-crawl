package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MapDaoJdbc implements MapDao {
    private DataSource dataSource;

    public MapDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(MapModel map, int stateId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO map (" +
                    "map_layout, " +
                    "is_current_map, " +
                    "game_level) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, map.getMapLayout());
            statement.setBoolean(2, map.isCurrentMap());
            statement.setInt(3, map.getGameLevel());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int mapId = resultSet.getInt(1);
            connectMapToGameState(conn, stateId, mapId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void update(MapModel map) {

    }

    @Override
    public MapModel get(int id) {
        return null;
    }

    @Override
    public List<MapModel> getAll() {
        return null;
    }
}
