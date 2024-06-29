package dev.nucleoid.backend.leaderboards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public record LeaderboardEntry(
        UUID player,
        int ranking,
        double value
) {
    public static final Codec<LeaderboardEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(UUID::fromString, UUID::toString).fieldOf("player").forGetter(LeaderboardEntry::player),
            Codec.INT.fieldOf("ranking").forGetter(LeaderboardEntry::ranking),
            Codec.DOUBLE.fieldOf("value").forGetter(LeaderboardEntry::value)
    ).apply(instance, LeaderboardEntry::new));

    public static LeaderboardEntry fromResultSet(ResultSet result) throws SQLException {
        var player = result.getObject("player_id", UUID.class);
        var ranking = result.getInt("ranking");
        var value = result.getDouble("value");
        return new LeaderboardEntry(player, ranking, value);
    }
}
