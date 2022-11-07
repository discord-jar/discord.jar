package com.seailz.javadiscordwrapper.model.guild.welcome;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a welcome screen channel recommendation.
 * @param channel The channel
 * @param description The description of the channel
 * @param emojiId
 * @param emojiName
 */
public record WelcomeScreenChannel(
        Channel channel,
        String description,
        String emojiId,
        String emojiName
) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("channel_id", channel.id())
                .put("description", description)
                .put("emoji_id", emojiId)
                .put("emoji_name", emojiName);
    }

    @NonNull
    public static WelcomeScreenChannel decompile(JSONObject obj, DiscordJv discordJv) {
        return new WelcomeScreenChannel(
                discordJv.getChannelById(obj.getString("channel_id")),
                obj.getString("description"),
                obj.getString("emoji_id"),
                obj.getString("emoji_name")
        );
    }
}
