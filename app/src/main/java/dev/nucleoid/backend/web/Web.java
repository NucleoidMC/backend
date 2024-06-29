package dev.nucleoid.backend.web;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.announcements.Announcements;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.leaderboards.Leaderboards;
import dev.nucleoid.backend.stagedoor.Stagedoor;
import dev.nucleoid.backend.web.auth.RoleAccessManager;
import dev.nucleoid.backend.web.exceptions.WebException;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Web {
    public static final Logger LOGGER = LoggerFactory.getLogger(Web.class);

    private final BackendConfig config;
    private final HikariDataSource dataSource;
    private final Leaderboards leaderboards;
    private final Announcements announcements;

    public Web(BackendConfig config, HikariDataSource dataSource) {
        this.config = config;
        this.dataSource = dataSource;
        this.leaderboards = new Leaderboards(config.web(), dataSource);
        this.announcements = new Announcements(dataSource);
    }

    public Javalin create() {
        return Javalin.create(javalinConfig -> {
                    javalinConfig.jetty.sessionHandler(() -> SessionUtil.sqlSessionHandler(dataSource));
                    javalinConfig.accessManager(new RoleAccessManager());
                    javalinConfig.plugins.enableRouteOverview("/route-overview");
                })
                .routes(() -> {
                    path("/v2", () -> {
                        get("/", ctx -> WebUtil.sendJson(ctx, new HelloWorld("world"), HelloWorld.CODEC));
                        path("/leaderboards", this.leaderboards);
                        path("/announcements", this.announcements);
                    });
                    path("/stagedoor", new Stagedoor(config.oauth(), this));
                })
                .exception(IllegalArgumentException.class, (e, ctx) -> WebUtil.error(ctx, 400, "bad request"))
                .exception(WebException.class, (e, ctx) -> {
                    if (e.getCause() != null) {
                        LOGGER.warn("error processing request", e);
                    }
                    WebUtil.error(ctx, e.getStatus(), e.getMessage());
                });
    }

    public Leaderboards getLeaderboards() {
        return leaderboards;
    }

    public Announcements getAnnouncements() {
        return announcements;
    }

    public record HelloWorld(String hello) {
        public static final Codec<HelloWorld> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("hello").forGetter(HelloWorld::hello)
        ).apply(instance, HelloWorld::new));
    }
}
