package com.seailz.discordjv.model.channel.forum;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.utils.Snowflake;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a forum channel tag
 *
 * @param id        the id of the tag
 * @param name      the name of the tag
 * @param moderated whether this tag can only be added to or removed from threads by a member with the MANAGE_THREADS permission
 * @param emojiId   the id of a guild's custom emoji
 * @param emojiName the unicode character of the emoji
 */
public record ForumTag(
        String id,
        String name,
        boolean moderated,
        String emojiId,
        String emojiName
) implements Compilerable, Snowflake {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("moderated", moderated)
                .put("emoji_id", emojiId)
                .put("emoji_name", emojiName);
    }

    @NonNull
    public static ForumTag decompile(JSONObject obj) {
        String id;
        String name;
        boolean moderated;
        String emojiId;
        String emojiName;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            moderated = obj.getBoolean("moderated");
        } catch (Exception e) {
            moderated = false;
        }

        try {
            emojiId = obj.getString("emoji_id");
        } catch (Exception e) {
            emojiId = null;
        }

        try {
            emojiName = obj.getString("emoji_name");
        } catch (Exception e) {
            emojiName = null;
        }

        return new ForumTag(id, name, moderated, emojiId, emojiName);
    }
}
