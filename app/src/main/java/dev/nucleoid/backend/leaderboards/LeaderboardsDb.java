package dev.nucleoid.backend.leaderboards;

import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.web.util.Pagination;

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

    public List<LeaderboardEntry> fetchLeaderboard(String id, Pagination pagination) throws SQLException {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("""
                        SELECT player_id, ranking, value
                                FROM leaderboard_rankings
                                WHERE leaderboard_id = ?
                                ORDER BY ranking ASC
                                OFFSET ?
                                LIMIT ?""")) {
            statement.setString(1, id);
            statement.setInt(2, pagination.offset());
            statement.setInt(3, pagination.count());
            var result = statement.executeQuery();
            // Prepare list with pre-allocated capacity
            var entries = new ArrayList<LeaderboardEntry>(pagination.count());
            while (result.next()) {
                entries.add(LeaderboardEntry.fromResultSet(result));
            }
            return entries;
        }
    }
}
