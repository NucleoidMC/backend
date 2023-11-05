package dev.nucleoid.backend.stagedoor;

import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.Web;
import dev.nucleoid.backend.web.exceptions.InternalServerException;
import dev.nucleoid.backend.web.exceptions.InvalidParameterException;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.rendering.template.TemplateUtil.model;

public class GithubAuth implements EndpointGroup {
    private static final URI GITHUB_CODE_EXCHANGE = URI.create("https://github.com/login/oauth/access_token");

    private final BackendConfig.OAuthConfig config;
    private final HttpClient client;

    public GithubAuth(BackendConfig.OAuthConfig config) {
        this.config = config;
        client = HttpClient.newBuilder()
                .build();
    }

    public void begin(Context ctx) {
        ctx.redirect("https://github.com/login/oauth/authorize?scope=user%3Aread+org%3Aread&prompt=consent&client_id=" + this.config.githubClientId());
    }

    public void callback(Context ctx) {
        var code = ctx.queryParam("code");
        if (code == null) throw new InvalidParameterException("code", "present");
        try {
            var accessToken = this.doCodeExchange(code);
            var user = this.getUser(accessToken);
            // TODO: team check
            ctx.sessionAttribute(Stagedoor.USERNAME_SESSION_ATTRIBUTE, user.login);
            ctx.redirect("/stagedoor/");
        } catch (IOException | InterruptedException e) {
            throw new InternalServerException(e);
        }
    }

    private String doCodeExchange(String code) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(GITHUB_CODE_EXCHANGE)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "client_id=" + this.config.githubClientId()
                                + "&client_secret=" + this.config.githubClientSecret()
                                + "&code=" + code
                ))
                .header("Accept", "application/json")
                .build();

        var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new InternalServerException(new Exception("code exchange failed"));
        }
        var json = JsonParser.parseString(response.body());
        var result = CodeExchangeResponse.CODEC.decode(JsonOps.INSTANCE, json).map(Pair::getFirst).result();
        if (result.isEmpty()) {
            throw new InternalServerException(new Exception("failed to parse code exchange response"));
        }
        return result.get().accessToken;
    }

    private GithubUser getUser(String accessToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.github.com/user"))
                .GET()
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            Web.LOGGER.info("status code: " + response.statusCode() + ", body: " + response.body());
            throw new InternalServerException(new Exception("failed to get user"));
        }
        var json = JsonParser.parseString(response.body());
        var result = GithubUser.CODEC.decode(JsonOps.INSTANCE, json).map(Pair::getFirst).result();
        if (result.isEmpty()) {
            throw new InternalServerException(new Exception("failed to parse user response"));
        }
        return result.get();
    }

    private void failed(Context ctx) {
        var message = ctx.queryParam("message");
        if (message == null) message = "generic";
        ctx.render("templates/stagedoor/github/failure.html.ftl", model("message", message));
    }

    @Override
    public void addEndpoints() {
        get("/begin", this::begin);
        get("/callback", this::callback);
        get("/failed", this::failed);
    }

    private record CodeExchangeResponse(
            String accessToken
    ) {
        public static final Codec<CodeExchangeResponse> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("access_token").forGetter(CodeExchangeResponse::accessToken)
        ).apply(instance, CodeExchangeResponse::new));
    }

    private record OrgMembership(
            GithubUser user,
            String state,
            String role
    ) {
        public static final Codec<OrgMembership> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                GithubUser.CODEC.fieldOf("user").forGetter(OrgMembership::user),
                Codec.STRING.fieldOf("state").forGetter(OrgMembership::state),
                Codec.STRING.fieldOf("role").forGetter(OrgMembership::role)
        ).apply(instance, OrgMembership::new));
    }

    private record GithubUser(
            String login
    ) {
        public static final Codec<GithubUser> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("login").forGetter(GithubUser::login)
        ).apply(instance, GithubUser::new));
    }
}
