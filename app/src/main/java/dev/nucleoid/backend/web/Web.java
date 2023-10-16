package dev.nucleoid.backend.web;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.leaderboards.Leaderboards;
import dev.nucleoid.backend.web.exceptions.WebException;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Web {
    public static Javalin create(
            BackendConfig.WebConfig config,
            HikariDataSource dataSource
    ) {
        return Javalin.create()
                .routes(() -> {
                    path("/v2", () -> {
                        get("/", ctx -> WebUtil.sendJson(ctx, new HelloWorld("world"), HelloWorld.CODEC));
                        path("/leaderboards", new Leaderboards(config, dataSource));
                    });
                })
                .exception(IllegalArgumentException.class, (e, ctx) -> WebUtil.error(ctx, 400, "bad request"))
                .exception(WebException.class, (e, ctx) -> WebUtil.error(ctx, e.getStatus(), e.getMessage()));
    }

    private record HelloWorld(String hello) {
        private static final Codec<HelloWorld> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("hello").forGetter(HelloWorld::hello)
        ).apply(instance, HelloWorld::new));
    }
}
