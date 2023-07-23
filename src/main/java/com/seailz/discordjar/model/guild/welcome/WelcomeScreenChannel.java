package com.seailz.discordjar.model.guild.welcome;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a welcome screen channel recommendation.
 *
 * @param channel     The channel
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
    public SJSONObject compile() {
        return new SJSONObject()
                .put("channel_id", channel.id())
                .put("description", description)
                .put("emoji_id", emojiId)
                .put("emoji_name", emojiName);
    }

    @NonNull
    public static WelcomeScreenChannel decompile(SJSONObject obj, DiscordJar discordJar) {
        return new WelcomeScreenChannel(
                discordJar.getChannelById(obj.getString("channel_id")),
                obj.getString("description"),
                obj.getString("emoji_id"),
                obj.getString("emoji_name")
        );
    }
}
