package dev.nucleoid.backend.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Migration {
    private final String name;

    protected Migration(String name) {
        this.name = name;
    }

    /**
     * Apply the migration to the given connection. Auto-commit will be disabled, and the transaction will only be
     * committed if this migration returns successfully.
     *
     * @param connection Connection to execute the migration on
     * @throws SQLException if an error occurs while performing the migration
     */
    public abstract void applyMigration(Connection connection) throws SQLException;

    /**
     * @return The name of this migration, which should be unique
     */
    public String getName() {
        return name;
    }
}
