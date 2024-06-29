package dev.nucleoid.backend.announcements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import dev.nucleoid.backend.web.util.Pagination;

public class AnnouncementsDb {
    private final HikariDataSource dataSource;

    public AnnouncementsDb(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Creates a new {@link Announcement} in the database and returns it. The postedOn timestamp will automatically be set to
     * the current timestamp when the announcement is created. 
     * @param message The message of the announcement
     * @param postedBy A display name for the author of the announcement 
     * @param hideAuthor Whether the name of the author should be shown publicly
     * @return The newly created announcement
     * @throws SQLException if the database operation fails
     */
    public Announcement createAnnouncement(String message, String postedBy, boolean hideAuthor) throws SQLException {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("""
                    INSERT INTO announcements(message, postedBy, hideAuthor)
                    VALUES(?, ?, ?)
                    RETURNING id, message, postedBy, postedOn, hideAuthor
                    """)) {
            statement.setString(1, message);
            statement.setString(2, postedBy);
            statement.setBoolean(3, hideAuthor);
            var result = statement.executeQuery();
            if (result.next()) {
                return Announcement.fromResultSet(result);
            } else {
                throw new IllegalStateException("failed to create announcement: database returned no rows");
            }
        }
    }

    public void editAnnouncement(Announcement announcement) throws SQLException {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("""
                    UPDATE announcements SET message = ?, postedBy = ?, postedOn = ?, hideAuthor = ?
                    WHERE id = ?
                    """)) {
            statement.setString(1, announcement.message());
            statement.setString(2, announcement.postedBy());
            statement.setTimestamp(3, Timestamp.from(announcement.postedOn().toInstant()));
            statement.setBoolean(4, announcement.hideAuthor());
            statement.setInt(5, announcement.id());
            int updated = statement.executeUpdate();
            if (updated != 1) {
                throw new IllegalStateException("tried to edit an announcement that does not exist");
            }
        }
    }

    public List<Announcement> getAnnouncements(Pagination pagination) throws SQLException {
        try (Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement("""
                    SELECT id, message, postedBy, postedOn, hideAuthor
                        FROM announcements
                        OFFSET ?
                        LIMIT ?
                    """)) {
            statement.setInt(1, pagination.offset());
            statement.setInt(2, pagination.count());
            var result = statement.executeQuery();
            var announcements = new ArrayList<Announcement>();
            while (result.next()) {
                announcements.add(Announcement.fromResultSet(result));
            }
            return announcements;
        }
    }
}
