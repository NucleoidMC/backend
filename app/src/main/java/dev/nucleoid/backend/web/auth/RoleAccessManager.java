package dev.nucleoid.backend.web.auth;

import dev.nucleoid.backend.NucleoidBackend;
import dev.nucleoid.backend.stagedoor.Stagedoor;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RoleAccessManager implements AccessManager {
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<? extends RouteRole> routeRoles) throws Exception {
        if (routeRoles.isEmpty()) {
            handler.handle(context);
            return;
        }

        // Stagedoor
        String stagedoorUser = context.sessionAttribute(Stagedoor.USERNAME_SESSION_ATTRIBUTE);
        if (stagedoorUser == null && routeRoles.contains(Role.STAGEDOOR_ADMIN)) {
            context.redirect("/stagedoor/github/begin");
            return;
        }

        // TODO: Integrations

        handler.handle(context);
    }
}
