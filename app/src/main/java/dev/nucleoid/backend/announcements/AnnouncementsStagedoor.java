package dev.nucleoid.backend.announcements;

import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.WebUtil;
import dev.nucleoid.backend.web.auth.Role;
import dev.nucleoid.backend.web.util.Pagination;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class AnnouncementsStagedoor implements EndpointGroup {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnouncementsStagedoor.class);

    private final AnnouncementsDb db;
    private final BackendConfig.WebConfig config;

    public AnnouncementsStagedoor(AnnouncementsDb db, BackendConfig.WebConfig config) {
        this.db = db;
        this.config = config;
    }

    private void listAnnouncements(Context ctx) {
        var pagination = Pagination.paginationQuery(ctx, config);
        try {
            var announcements = db.getAnnouncements(pagination);

        } catch (SQLException e) {
            LOGGER.warn("failed to get announcements", e);
            WebUtil.internalError(ctx);
        }
    }

    @Override
    public void addEndpoints() {
        path("/", () -> get(this::listAnnouncements, Role.STAGEDOOR_ADMIN));
    }
}
