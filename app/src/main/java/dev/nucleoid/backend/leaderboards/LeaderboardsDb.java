package dev.nucleoid.backend.leaderboards;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardsDb {
    private final HikariDataSource dataSource;

    public LeaderboardsDb(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<LeaderboardEntry> fetchLeaderboard(String id, int count, int offset) throws SQLException {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("""
                        SELECT player_id, ranking, value
                                FROM leaderboard_rankings
                                WHERE leaderboard_id = ?
                                ORDER BY ranking ASC
                                OFFSET ?
                                LIMIT ?""")) {
            statement.setString(1, id);
            statement.setInt(2, offset);
            statement.setInt(3, count);
            var result = statement.executeQuery();
            // Prepare list with pre-allocated capacity
            var entries = new ArrayList<LeaderboardEntry>(count);
            while (result.next()) {
                var player = result.getObject("player_id", UUID.class);
                var ranking = result.getInt("ranking");
                var value = result.getDouble("value");
                entries.add(new LeaderboardEntry(player, ranking, value));
            }
            return entries;
        }
    }
}
