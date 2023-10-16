package dev.nucleoid.backend;

import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.database.DbConnection;
import dev.nucleoid.backend.database.Migrations;
import dev.nucleoid.backend.web.Web;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class NucleoidBackend {
    public static final Logger LOGGER = LoggerFactory.getLogger(NucleoidBackend.class);

    public static void main(String[] args) {
        BackendConfig config = BackendConfig.load();
        HikariDataSource dataSource = DbConnection.createDataSource(config.database());
        try {
            Migrations.runMigrations(dataSource);
        } catch (SQLException e) {
            LOGGER.error("Failed to apply migrations", e);
            System.exit(1);
            return;
        }

        Web.create(config.web(), dataSource)
                .start(3000);
    }
}
