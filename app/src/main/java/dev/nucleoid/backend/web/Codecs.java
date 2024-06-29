package dev.nucleoid.backend.web;

import com.mojang.serialization.Codec;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Codecs {
    public static final Codec<OffsetDateTime> OFFSET_DATE_TIME = Codec.STRING.xmap(OffsetDateTime::parse, OffsetDateTime::toString);
    public static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
}
