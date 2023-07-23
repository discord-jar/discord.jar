package com.seailz.discordjar.model.message;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a reference to a message
 *
 * @param messageId       The id of the message
 * @param channelId       The id of the channel the message was sent in
 * @param guildId         The id of the guild the message was sent in
 * @param failIfNotExists when sending, whether to error if the referenced message doesn't exist instead of sending as a normal (non-reply) message, default true
 */
public record MessageReference(
        String messageId,
        String channelId,
        String guildId,
        boolean failIfNotExists
) implements Compilerable {

    @Override
    public SJSONObject compile() {
        SJSONObject obj = new SJSONObject();
        obj.put("message_id", messageId);
        obj.put("channel_id", channelId);
        obj.put("guild_id", guildId);
        obj.put("fail_if_not_exists", failIfNotExists);
        return obj;
    }

    @NonNull
    public static MessageReference decompile(SJSONObject obj) {
        String messageId;
        String channelId;
        String guildId;
        boolean failIfNotExists;

        try {
            messageId = obj.getString("message_id");
        } catch (Exception e) {
            messageId = null;
        }

        try {
            channelId = obj.getString("channel_id");
        } catch (Exception e) {
            channelId = null;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (Exception e) {
            guildId = null;
        }

        try {
            failIfNotExists = obj.getBoolean("fail_if_not_exists");
        } catch (Exception e) {
            failIfNotExists = true;
        }
        return new MessageReference(messageId, channelId, guildId, failIfNotExists);
    }
}
