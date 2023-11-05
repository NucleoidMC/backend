package dev.nucleoid.backend.stagedoor;

import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.auth.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import dev.nucleoid.backend.web.Web;
import dev.nucleoid.backend.web.WebUtil;
import io.javalin.http.Context;

public class Stagedoor implements EndpointGroup {
    public static final String USERNAME_SESSION_ATTRIBUTE = "stagedoor-username";

    private final BackendConfig.OAuthConfig oauthConfig;

    public Stagedoor(BackendConfig.OAuthConfig oauthConfig) {
        this.oauthConfig = oauthConfig;
    }

    private void index(Context ctx) {
        var username = ctx.sessionAttribute(USERNAME_SESSION_ATTRIBUTE);
        ctx.html("hello, <b>" + username + "</b>");
    }

    @Override
    public void addEndpoints() {
        get("/", this::index, Role.STAGEDOOR_ADMIN);
        get("/hello", ctx -> WebUtil.sendJson(ctx, new Web.HelloWorld("world"), Web.HelloWorld.CODEC), Role.STAGEDOOR_ADMIN);
        path("/github", new GithubAuth(this.oauthConfig));
    }
}
