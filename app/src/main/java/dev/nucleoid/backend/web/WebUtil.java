package dev.nucleoid.backend.web;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nucleoid.backend.NucleoidBackend;
import dev.nucleoid.backend.web.exceptions.InvalidParameterException;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Optional;

public class WebUtil {
    public static int intQuery(Context ctx, String name, int defaultValue) throws InvalidParameterException {
        var value = defaultValue;
        try {
            var query = ctx.queryParam(name);
            if (query != null) {
                value = Integer.parseInt(query);
            }
        } catch (NumberFormatException e) {
            throw new InvalidParameterException(name, "an int");
        }
        return value;
    }

    public static <T> void sendJson(Context ctx, T object, Codec<T> codec) {
        sendJson(ctx, HttpStatus.OK, object, codec);
    }

    public static <T> void sendJson(Context ctx, HttpStatus status, T object, Codec<T> codec) {
        sendJson(ctx, status.getCode(), object, codec);
    }

    public static <T> void sendJson(Context ctx, int status, T object, Codec<T> codec) {
        Optional<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, object).result();
        if (result.isPresent()) {
            ctx.status(status)
                    .contentType("application/json")
                    .result(result.get().toString());
        } else {
            NucleoidBackend.LOGGER.warn("failed to serialize {}", object);
            internalError(ctx);
        }
    }

    public static void error(Context ctx, int status, String message) {
        sendJson(ctx, status, new Error(true, status, message), Error.CODEC);
    }

    public static void internalError(Context ctx) {
        ctx.contentType("application/json")
                .status(500)
                .result("{\"error\": true,\"code\": 500,\"message\": \"internal server error\"}");
    }

    private record Error(
            boolean error,
            int code,
            String message
    ) {
        private static final Codec<Error> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("error").forGetter(Error::error),
                Codec.INT.fieldOf("code").forGetter(Error::code),
                Codec.STRING.fieldOf("message").forGetter(Error::message)
        ).apply(instance, Error::new));
    }
}
