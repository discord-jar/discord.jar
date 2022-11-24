package com.seailz.discordjv.model.channel.thread;

import com.seailz.discordjv.core.Compilerable;
import org.json.JSONObject;

/**
 * Represents a thread member object
 *
 * @param id            the id of the thread
 * @param userId        the id of the user
 * @param joinTimestamp the time the current user joined the thread
 * @param flags         any user-thread settings, currently only used for notifications
 */
public record ThreadMember(
        String id,
        String userId,
        String joinTimestamp,
        int flags
) implements Compilerable {


    @Override
    public JSONObject compile() {
        return null;
    }

    public static ThreadMember decompile(JSONObject obj) {
        String id;
        String userId;
        String joinTimestamp;
        int flags;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            userId = obj.getString("user_id");
        } catch (Exception e) {
            userId = null;
        }

        try {
            joinTimestamp = obj.getString("join_timestamp");
        } catch (Exception e) {
            joinTimestamp = null;
        }

        try {
            flags = obj.getInt("flags");
        } catch (Exception e) {
            flags = 0;
        }

        return new ThreadMember(id, userId, joinTimestamp, flags);
    }
}
