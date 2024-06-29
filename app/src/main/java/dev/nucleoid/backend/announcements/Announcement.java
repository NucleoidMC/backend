package dev.nucleoid.backend.announcements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nucleoid.backend.web.Codecs;

public record Announcement(
    int id,
    String message,
    String postedBy,
    OffsetDateTime postedOn,
    boolean hideAuthor
) {
    public static final Codec<Announcement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("id").forGetter(Announcement::id),
        Codec.STRING.fieldOf("message").forGetter(Announcement::message),
        Codec.STRING.fieldOf("posted_by").forGetter(Announcement::postedBy),
        Codecs.OFFSET_DATE_TIME.fieldOf("posted_on").forGetter(Announcement::postedOn),
        Codec.BOOL.fieldOf("hide_author").forGetter(Announcement::hideAuthor)
    ).apply(instance, Announcement::new));

    public static Announcement fromResultSet(ResultSet result) throws SQLException {
        var id = result.getInt("id");
        var message = result.getString("message");
        var postedBy = result.getString("postedBy");
        var postedOn = result.getTimestamp("postedOn").toInstant().atOffset(ZoneOffset.of("Z"));
        var hideAuthor = result.getBoolean("hideAuthor");
        return new Announcement(id, message, postedBy, postedOn, hideAuthor);
    }
}
