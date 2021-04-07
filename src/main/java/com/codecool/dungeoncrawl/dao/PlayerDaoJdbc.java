package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (" +
                    "player_name, " +
                    "hp, " +
                    "x, " +
                    "y, " +
                    "experience, " +
                    "strength, " +
                    "poison_count, " +
                    "inventory) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setVariablesAndExecuteUpdate(player, conn, statement);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET " +
                    "player_name = ?, " +
                    "hp = ?, " +
                    "x = ?, " +
                    "y = ?, " +
                    "experience = ?, " +
                    "strength = ?, " +
                    "poison_count = ?, " +
                    "inventory = ? " +
                    "WHERE id = ? ";
            PreparedStatement statement = conn.prepareStatement(sql);
            setVariablesAndExecuteUpdate(player, conn, statement);
            statement.setInt(9, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "player_name, " +
                    "hp, " +
                    "x, " +
                    "y, " +
                    "experience, " +
                    "strength, " +
                    "poison_count, " +
                    "inventory " +
                    "FROM player WHERE id = ? ";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                return null;
            }
            return setVariablesAndCreatePlayerModel(id, rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT " +
                    "player_name, " +
                    "hp, " +
                    "x, " +
                    "y, " +
                    "experience, " +
                    "strength, " +
                    "poison_count, " +
                    "inventory " +
                    " FROM player ";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            List<PlayerModel> result = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt(1);

                PlayerModel playerModel = setVariablesAndCreatePlayerModel(id, rs);
                result.add(playerModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setVariablesAndExecuteUpdate(PlayerModel player, Connection conn, PreparedStatement statement) throws SQLException{
        Array inventoryArray = conn.createArrayOf("text", player.getInventory().toArray());
        statement.setString(1, player.getPlayerName());
        statement.setInt(2, player.getHp());
        statement.setInt(3, player.getX());
        statement.setInt(4, player.getY());
        statement.setInt(5, player.getExperience());
        statement.setInt(6, player.getStrength());
        statement.setInt(7, player.getPoisonCount());
        statement.setArray(8, inventoryArray);
    }

    private PlayerModel setVariablesAndCreatePlayerModel(int id, ResultSet rs) throws SQLException {
        String playerName = rs.getString(1);
        int hp = rs.getInt(2);
        int x = rs.getInt(3);
        int y = rs.getInt(4);
        int experience = rs.getInt(5);
        int strength = rs.getInt(6);
        int poisonCount = rs.getInt(7);
        Array inventory = rs.getArray(8);
        String[] inventoryJavaArray = (String[])inventory.getArray();
        List<String> inventoryList = Arrays.asList(inventoryJavaArray);

        PlayerModel playerModel = new PlayerModel(
                playerName,
                hp,
                x,
                y,
                experience,
                strength,
                poisonCount,
                inventoryList);
        playerModel.setId(id);
        return playerModel;
    }
}
