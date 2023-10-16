package dev.nucleoid.backend.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.config.BackendConfig;

import java.io.PrintWriter;
import java.util.Properties;

public class DbConnection {
    public static HikariDataSource createDataSource(BackendConfig.DatabaseConfig config) {
        Properties props = new Properties();

        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        props.setProperty("dataSource.ssl", "false");
        props.setProperty("dataSource.serverName", config.host());
        props.setProperty("dataSource.portNumber", config.port());
        props.setProperty("dataSource.user", config.username());
        props.setProperty("dataSource.password", config.password());
        props.setProperty("dataSource.databaseName", config.databaseName());
        props.put("dataSource.logWriter", new PrintWriter(System.out));

        HikariConfig hikariConfig = new HikariConfig(props);
        return new HikariDataSource(hikariConfig);
    }
}
