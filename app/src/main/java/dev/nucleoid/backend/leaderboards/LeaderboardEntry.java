package dev.nucleoid.backend.leaderboards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

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
}
