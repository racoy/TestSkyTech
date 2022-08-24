package org.example.testTask;

import org.example.ConnectionPool;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;

import static org.example.DatabaseParameters.*;

public enum ClanRepository {
    INSTANCE;
    private final static int BATCH_SIZE = 10;

    public boolean saveClanToDb(Clan clan) {
        try (Connection conn = ConnectionPool.INSTANCE.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            String sql = getSqlQueryForUpdateClan(clan);
            return stmt.executeUpdate(sql) != 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int updateClansInDb(Collection<Clan> clans) {
        if (clans.isEmpty()) return 0;
        int res = 0;
        try (Connection conn = ConnectionPool.INSTANCE.getConnection()) {
            conn.setAutoCommit(false);
            int i = 0;
            try (Statement stmt = conn.createStatement()) {
                for (Clan clan : clans) {
                    i++;
                    stmt.addBatch(getSqlQueryForUpdateClan(clan));
                    if (i % BATCH_SIZE == 0 || i == clans.size()) {
                        try {
                            int[] result = stmt.executeBatch();
                            res += Arrays.stream(result).sum();
                            conn.commit();
                        } catch (BatchUpdateException ex) {
                            ex.printStackTrace();
                            conn.rollback();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String getSqlQueryForUpdateClan(Clan clan) {
        return "UPDATE " + CLAN_TABLE_NAME + " SET name = " + clan.getName() + ", gold = " + clan.getGold() + " WHERE id = "+ clan.getId() + ";" ;
    }

    public Clan getClanFromDb(long clanId) {
        final String query = "SELECT id, name, gold FROM " + CLAN_TABLE_NAME + " WHERE id = " + clanId + "";

        try (Connection conn = ConnectionPool.INSTANCE.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)
        ) {
            if (rs.next()) {
                Clan clan = new Clan(rs.getLong("id"));
                clan.setName(rs.getString("name"));
                clan.setGold(rs.getInt("gold"));
                return clan;
            }
        } catch (SQLException e) {
            e.printStackTrace();    //тут можно было бы делать повоторный запрос, или пробрасывать исключение, но я не знаю как лучше
        }
        return null;
    }
}
