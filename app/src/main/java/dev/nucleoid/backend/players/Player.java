package dev.nucleoid.backend.players;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nucleoid.backend.web.Codecs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public record Player(
        UUID id,
        String lastKnownName,
        OffsetDateTime firstSeen,
        OffsetDateTime lastSeen
) {
    public static final Codec<Player> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.UUID_CODEC.fieldOf("id").forGetter(Player::id),
            Codec.STRING.fieldOf("lastKnownName").forGetter(Player::lastKnownName),
            Codecs.OFFSET_DATE_TIME.fieldOf("firstSeen").forGetter(Player::firstSeen),
            Codecs.OFFSET_DATE_TIME.fieldOf("lastSeen").forGetter(Player::lastSeen)
    ).apply(instance, Player::new));

    public static Player fromResultSet(ResultSet result) throws SQLException {
        var id = result.getObject("id", UUID.class);
        var lastKnownName = result.getString("lastKnownName");
        var firstSeen = result.getTimestamp("firstSeen").toInstant().atOffset(ZoneOffset.of("Z"));
        var lastSeen = result.getTimestamp("lastSeen").toInstant().atOffset(ZoneOffset.of("Z"));
        return new Player(id, lastKnownName, firstSeen, lastSeen);
    }
}
