package com.seailz.javadiscordwrapper.model.channel.thread;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a thread metadata object.
 * @param archived              whether the thread is archived
 * @param autoArchiveDuration 	the thread will stop showing in the channel list after auto_archive_duration minutes of inactivity, can be set to: 60, 1440, 4320, 10080
 * @param archiveTimestamp 	    timestamp when the thread's archive status was last changed, used for calculating recent activity
 * @param locked 	            whether the thread is locked; when a thread is locked, only users with MANAGE_THREADS can unarchive it
 * @param invitable             whether non-moderators can add other non-moderators to a thread; only available on private threads
 * @param createTimestamp 	    timestamp when the thread was created; only populated for threads created after 2022-01-09
 */
public record ThreadMetadata(
        boolean archived,
        int autoArchiveDuration,
        String archiveTimestamp,
        boolean locked,
        int invitable,
        String createTimestamp
) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("archived", archived)
                .put("auto_archive_duration", autoArchiveDuration)
                .put("archive_timestamp", archiveTimestamp)
                .put("locked", locked)
                .put("invitable", invitable)
                .put("create_timestamp", createTimestamp);
    }

    @NonNull
    public static ThreadMetadata decompile(JSONObject obj) {
        boolean archived;
        int autoArchiveDuration;
        String archiveTimestamp;
        boolean locked;
        int invitable;
        String createTimestamp;

        try {
            archived = obj.getBoolean("archived");
        } catch (Exception e) {
            archived = false;
        }

        try {
            autoArchiveDuration = obj.getInt("auto_archive_duration");
        } catch (Exception e) {
            autoArchiveDuration = 0;
        }

        try {
            archiveTimestamp = obj.getString("archive_timestamp");
        } catch (Exception e) {
            archiveTimestamp = null;
        }

        try {
            locked = obj.getBoolean("locked");
        } catch (Exception e) {
            locked = false;
        }

        try {
            invitable = obj.getInt("invitable");
        } catch (Exception e) {
            invitable = 0;
        }

        try {
            createTimestamp = obj.getString("create_timestamp");
        } catch (Exception e) {
            createTimestamp = null;
        }

        return new ThreadMetadata(archived, autoArchiveDuration, archiveTimestamp, locked, invitable, createTimestamp);
    }
}
