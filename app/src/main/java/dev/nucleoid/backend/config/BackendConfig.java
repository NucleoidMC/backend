package dev.nucleoid.backend.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nucleoid.backend.NucleoidBackend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public record BackendConfig(
    DatabaseConfig database,
    WebConfig web
) {
    public static Codec<BackendConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DatabaseConfig.CODEC.fieldOf("database").forGetter(BackendConfig::database),
            WebConfig.CODEC.fieldOf("web").forGetter(BackendConfig::web)
    ).apply(instance, BackendConfig::new));

    public BackendConfig() {
        this(new DatabaseConfig(), new WebConfig());
    }

    public static BackendConfig load() {
        Path path = Path.of("nucleoid_backend.json");
        if (!Files.exists(path)) {
            BackendConfig config = new BackendConfig();
            Optional<JsonElement> result = CODEC.encodeStart(JsonOps.INSTANCE, config).result();
            if (result.isPresent()) {
                try {
                    Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(result.get()));
                } catch (IOException e) {
                    NucleoidBackend.LOGGER.warn("Failed to save default config!", e);
                }
            } else {
                NucleoidBackend.LOGGER.warn("Failed to save default config!");
            }
            return new BackendConfig();
        } else {
            try {
                String s = Files.readString(path);
                JsonElement ele = JsonParser.parseString(s);
                DataResult<BackendConfig> result = CODEC.decode(JsonOps.INSTANCE, ele).map(Pair::getFirst);
                Optional<DataResult.PartialResult<BackendConfig>> err = result.error();
                err.ifPresent(e -> NucleoidBackend.LOGGER.warn("Failed to load config: {}", e.message()));
                return result.result().orElseGet(BackendConfig::new);
            } catch (IOException e) {
                NucleoidBackend.LOGGER.warn("Failed to load config!", e);
                return new BackendConfig();
            }
        }
    }

    public record WebConfig(
            int maxQuerySize
    ) {
        public static final Codec<WebConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("max_query_size").forGetter(WebConfig::maxQuerySize)
        ).apply(instance, WebConfig::new));

        public WebConfig() {
            this(50);
        }
    }

    public record DatabaseConfig(
            String host,
            String port,
            String username,
            String password,
            String databaseName
    ) {
        public static Codec<DatabaseConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("host").forGetter(DatabaseConfig::host),
                Codec.STRING.fieldOf("port").forGetter(DatabaseConfig::port),
                Codec.STRING.fieldOf("username").forGetter(DatabaseConfig::username),
                Codec.STRING.fieldOf("password").forGetter(DatabaseConfig::password),
                Codec.STRING.fieldOf("database_name").forGetter(DatabaseConfig::databaseName)
        ).apply(instance, DatabaseConfig::new));

        public DatabaseConfig() {
            this("localhost", "5432", "postgres", "postgres", "nucleoid_backend");
        }
    }
}
