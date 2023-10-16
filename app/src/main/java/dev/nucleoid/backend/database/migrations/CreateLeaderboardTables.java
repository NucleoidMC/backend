package dev.nucleoid.backend.database.migrations;

import dev.nucleoid.backend.database.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateLeaderboardTables extends Migration {
    private static final String CREATE_ENTRIES_TABLE = """
    CREATE TABLE leaderboard_rankings(
        player_id uuid NOT NULL,
        leaderboard_id text NOT NULL,
        ranking bigint NOT NULL,
        value double precision NOT NULL,
        PRIMARY KEY (player_id, leaderboard_id)
    );""";

    public CreateLeaderboardTables() {
        super("CreateLeaderboardTables");
    }

    @Override
    public void applyMigration(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_ENTRIES_TABLE)) {
            statement.execute();
        }
    }
}
