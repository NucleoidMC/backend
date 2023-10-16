package dev.nucleoid.backend.leaderboards;

import com.mojang.serialization.Codec;
import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.WebUtil;
import dev.nucleoid.backend.web.exceptions.InvalidParameterException;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Leaderboards implements EndpointGroup {
    public static final Logger LOGGER = LoggerFactory.getLogger(Leaderboards.class);

    private final LeaderboardsDb db;
    private final BackendConfig.WebConfig config;

    public Leaderboards(BackendConfig.WebConfig config, HikariDataSource dataSource) {
        this.db = new LeaderboardsDb(dataSource);
        this.config = config;
    }

    private void getLeaderboard(Context ctx) {
        var id = ctx.pathParam("id");
        int count = WebUtil.intQuery(ctx, "count", 10);
        int offset = WebUtil.intQuery(ctx, "offset", 0);

        // Quick checks before we attempt querying the DB
        if (!id.contains(":")) {
            throw new InvalidParameterException("id");
        }
        if (count > config.maxQuerySize()) {
            throw new InvalidParameterException("count", "<=" + config.maxQuerySize());
        }
        if (count <= 0) {
            throw new InvalidParameterException("count", ">0");
        }
        if (offset < 0) {
            throw new InvalidParameterException("count", ">=0");
        }

        // Make the query
        try {
            var results = this.db.fetchLeaderboard(id, count, offset);
            WebUtil.sendJson(ctx, results, Codec.list(LeaderboardEntry.CODEC));
        } catch (SQLException e) {
            LOGGER.error("failed to query leaderboard", e);
            WebUtil.internalError(ctx);
        }
    }

    private void getRankings(Context ctx) {

    }

    @Override
    public void addEndpoints() {
        path("/{id}", () -> get(this::getLeaderboard));
    }
}
