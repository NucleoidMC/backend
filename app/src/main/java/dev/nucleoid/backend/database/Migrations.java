package dev.nucleoid.backend.database;

import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.database.migrations.CreateAnnouncementsTable;
import dev.nucleoid.backend.database.migrations.CreateLeaderboardTables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Migrations {
    public static final Logger LOGGER = LoggerFactory.getLogger(Migrations.class);

    /**
     * List of all migrations for this project. They will be applied in the order specified.
     */
    private static final List<Migration> MIGRATIONS = List.of(
            new CreateLeaderboardTables(),
            new CreateAnnouncementsTable()
    );

    public static void runMigrations(HikariDataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.prepareStatement("""
            CREATE TABLE IF NOT EXISTS __migrations_state(
                name text UNIQUE NOT NULL
            );
            """).execute();

            try (PreparedStatement migrationAppliedStatement = conn.prepareStatement("SELECT name FROM __migrations_state WHERE name = ?");
                    PreparedStatement migrationCompleteStatement = conn.prepareStatement("INSERT INTO __migrations_state(name) VALUES(?)")) {
                for (var migration : MIGRATIONS) {
                    migrationAppliedStatement.setString(1, migration.getName());
                    migrationCompleteStatement.setString(1, migration.getName());
                    try (var result = migrationAppliedStatement.executeQuery()) {
                        if (result.next()) {
                            // Migration already applied, skip
                            LOGGER.info("Migration {} has already been applied", migration.getName());
                        } else{
                            LOGGER.info("Running migration {}...", migration.getName());
                            conn.setAutoCommit(false);
                            try {
                                migration.applyMigration(conn);
                                migrationCompleteStatement.executeUpdate();
                            } catch (SQLException e) {
                                LOGGER.error("Failed to apply migration " + migration.getName() + ":", e);
                                conn.rollback();
                                throw e;
                            } finally {
                                conn.commit();
                                conn.setAutoCommit(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
