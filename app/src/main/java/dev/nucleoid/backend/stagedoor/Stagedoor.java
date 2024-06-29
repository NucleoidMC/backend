package dev.nucleoid.backend.stagedoor;

import dev.nucleoid.backend.announcements.AnnouncementsStagedoor;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.auth.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.rendering.template.TemplateUtil.model;

import dev.nucleoid.backend.web.Web;
import dev.nucleoid.backend.web.WebUtil;
import io.javalin.http.Context;

public class Stagedoor implements EndpointGroup {
    public static final String USERNAME_SESSION_ATTRIBUTE = "stagedoor-username";

    private final BackendConfig config;
    private final Web web;

    public Stagedoor(BackendConfig config, Web web) {
        this.config = config;
        this.web = web;
    }

    private void index(Context ctx) {
        var username = ctx.sessionAttribute(USERNAME_SESSION_ATTRIBUTE);
        ctx.render("templates/stagedoor/index.html.ftl", model("username", username));
    }

    private void confirmLogout(Context ctx) {
        ctx.render("templates/stagedoor/logout.html.ftl");
    }

    private void logout(Context ctx) {
        ctx.consumeSessionAttribute(USERNAME_SESSION_ATTRIBUTE);
        ctx.redirect("https://nucleoid.xyz");
    }

    @Override
    public void addEndpoints() {
        get("/", this::index, Role.STAGEDOOR_ADMIN);
        get("/hello", ctx -> WebUtil.sendJson(ctx, new Web.HelloWorld("world"), Web.HelloWorld.CODEC), Role.STAGEDOOR_ADMIN);
        get("/logout", this::confirmLogout);
        post("/logout", this::logout);
        path("/github", new GithubAuth(this.config.oauth()));
        path("/announcements", new AnnouncementsStagedoor(this.web.getAnnouncements().getDb(), this.config.web()));
    }
}
