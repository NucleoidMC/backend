package dev.nucleoid.backend.database.migrations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.nucleoid.backend.database.Migration;

public class CreateAnnouncementsTable extends Migration {
    private static final String CREATE_ANNOUNCEMENTS_TABLE = """
    CREATE TABLE announcements(
        id SERIAL NOT NULL,
        message text NOT NULL,
        postedBy text NOT NULL,
        postedOn timestamp with time zone NOT NULL DEFAULT now(),
        hideAuthor boolean NOT NULL DEFAULT false
    );""";

    public CreateAnnouncementsTable() {
        super("CreateAnnouncementsTable");
    }

    @Override
    public void applyMigration(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_ANNOUNCEMENTS_TABLE)) {
            statement.execute();
        }
    }
}
