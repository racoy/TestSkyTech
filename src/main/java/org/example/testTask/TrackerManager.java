package org.example.testTask;

import org.example.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static org.example.DatabaseParameters.*;

public class TrackerManager {
    public static void trackerClanGold(long clanId, long userId, int gold) {
        try (Connection conn = ConnectionPool.INSTANCE.getConnection();
             Statement stmt = conn.createStatement();
        ) {
            String sql = String.format(
                    "INSERT INTO %s (clanId, userId, gold, creationtimestamp) VALUES (%s, %s, %s, '%s')",
                    TRACKER_TABLE_NAME, clanId, userId, gold, LocalDateTime.now());
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
