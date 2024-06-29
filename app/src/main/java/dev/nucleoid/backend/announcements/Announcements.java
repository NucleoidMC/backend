package dev.nucleoid.backend.announcements;

import com.zaxxer.hikari.HikariDataSource;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import static dev.nucleoid.backend.web.WebUtil.uuidPathParam;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Announcements implements EndpointGroup {
    private final AnnouncementsDb db;

    public Announcements(HikariDataSource dataSource) {
        this.db = new AnnouncementsDb(dataSource);
    }

    private void getAnnouncements(Context ctx) {
        var id = uuidPathParam(ctx, "player");

    }

    public AnnouncementsDb getDb() {
        return db;
    }

    @Override
    public void addEndpoints() {
        path("/{player}", () -> get(this::getAnnouncements));
    }
}
