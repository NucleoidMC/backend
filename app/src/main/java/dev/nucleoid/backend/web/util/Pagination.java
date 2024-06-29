package dev.nucleoid.backend.web.util;

import dev.nucleoid.backend.config.BackendConfig;
import dev.nucleoid.backend.web.WebUtil;
import dev.nucleoid.backend.web.exceptions.InvalidParameterException;
import io.javalin.http.Context;

public record Pagination(
        int count,
        int offset
) {
    public static Pagination paginationQuery(Context ctx, BackendConfig.WebConfig config) {
        int count = WebUtil.intQuery(ctx, "count", 10);
        int offset = WebUtil.intQuery(ctx, "offset", 0);

        if (count > config.maxQuerySize()) {
            throw new InvalidParameterException("count", "<=" + config.maxQuerySize());
        }
        if (count <= 0) {
            throw new InvalidParameterException("count", ">0");
        }
        if (offset < 0) {
            throw new InvalidParameterException("offset", ">=0");
        }

        return new Pagination(count, offset);
    }
}
