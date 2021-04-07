package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MapDaoJdbc implements MapDao {
    private DataSource dataSource;

    public MapDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(MapModel map, int stateId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_map (" +
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

    @Override
    public void update(MapModel map, int stateId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_map SET" +
                    " map_layout = ?, " +
                    "is_current_map = ? " +
                    "FROM maps " +
                    "WHERE game_map.id = maps.map_id " +
                    "AND maps.game_state_id = ? " +
                    "AND game_map.id = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, map.getMapLayout());
            statement.setBoolean(2, map.isCurrentMap());
            statement.setInt(3, stateId);
            statement.setInt(4, map.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public MapModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "game_map.map_layout, " +
                    "game_map.is_current_map, " +
                    "game_map.game_level, " +
                    "maps.game_state_id, " +
                    "FROM game_map " +
                    "JOIN maps " +
                    "ON game_map.id = maps.map_id " +
                    "WHERE id = ? ";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int gameStateId = rs.getInt(1);
            String mapLayout = rs.getString(2);
            boolean currentMap = rs.getBoolean(3);
            int gameLevel = rs.getInt(4);

            return new MapModel(gameStateId, mapLayout, currentMap, gameLevel);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MapModel> getAll(int stateId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "game_map.id, " +
                    "game_map.map_layout, " +
                    "game_map.is_current_map, " +
                    "game_map.game_level " +
                    "FROM game_map " +
                    "JOIN maps " +
                    "ON game_map.id = maps.map_id " +
                    "WHERE maps.game_state_id = ? ";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, stateId);
            ResultSet rs = st.executeQuery();

            List<MapModel> result = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);

                String mapLayout = rs.getString(2);
                boolean currentMap = rs.getBoolean(3);
                int gameLevel = rs.getInt(4);

                MapModel mapModel = new MapModel(stateId, mapLayout, currentMap, gameLevel);
                mapModel.setId(id);
                result.add(mapModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectMapToGameState(Connection conn, int stateId, int mapId) throws SQLException {
        String sql = "INSERT INTO maps" +
                "(game_state_id, " +
                "map_id) " +
                "VALUES (?, ?) ";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, stateId);
        statement.setInt(2, mapId);
        statement.executeUpdate();
    }
}
