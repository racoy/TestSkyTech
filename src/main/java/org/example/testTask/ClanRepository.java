package org.example.testTask;

import org.example.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.example.DatabaseParameters.*;

public enum ClanRepository {
    INSTANCE;
    private final static int CASE_SIZE = 10;

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

    public void updateClansGoldInDb(Collection<Clan> clansForUpdate) {
        if (clansForUpdate.isEmpty()) return;
        ArrayList<Clan> clans = new ArrayList<>(clansForUpdate);

        try (Connection conn = ConnectionPool.INSTANCE.getConnection();
             Statement stmt = conn.createStatement()) {
            int i = 0;
            while (i + CASE_SIZE < clans.size()) {
                String query = getQueryForUpdateGoldInClans(clans.subList(i, i + CASE_SIZE));
                System.out.println(query);
                stmt.executeUpdate(query);
                i += CASE_SIZE;
            }
            String query = getQueryForUpdateGoldInClans(clans.subList(i, clans.size()));
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getQueryForUpdateGoldInClans(List<Clan> clans) {
        StringBuilder queryFirstPart = new StringBuilder("UPDATE " + CLAN_TABLE_NAME + " SET gold = CASE ");
        StringBuilder querySecondPart = new StringBuilder("END WHERE id IN (");
        for (Clan clan : clans) {
            queryFirstPart.append(String.format("WHEN id = %s THEN %s ", clan.getId(), clan.getGold()));
            querySecondPart.append(clan.getId());
            querySecondPart.append(", ");
        }
        querySecondPart.deleteCharAt(querySecondPart.length() - 1);
        querySecondPart.deleteCharAt(querySecondPart.length() - 1);
        querySecondPart.append(')');
        return queryFirstPart.append(querySecondPart).toString();
    }

    private String getSqlQueryForUpdateClan(Clan clan) {
        return "UPDATE " + CLAN_TABLE_NAME + " SET name = " + clan.getName() + ", gold = " + clan.getGold() + " WHERE id = " + clan.getId() + ";";
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
