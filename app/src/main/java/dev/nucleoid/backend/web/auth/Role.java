package dev.nucleoid.backend.web.auth;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    STAGEDOOR_ADMIN("stagedoor_admin"),
    INTEGRATIONS_CLIENT("integrations_client"),
    ;
    private final String name;
    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
