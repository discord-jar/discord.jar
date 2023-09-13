package com.seailz.discordjar.model.guild.welcome;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.utils.model.DiscordJarProp;
import com.seailz.discordjar.utils.model.JSONProp;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a welcome screen channel recommendation.
 */
public class WelcomeScreenChannel {
    private Channel channel;
    @JSONProp("channel_id")
    private String channelId;
    @JSONProp("description")
    private String description;
    @JSONProp("emoji_id")
    private String emojiId;
    @JSONProp("emoji_name")
    private String emojiName;
    @DiscordJarProp
    private DiscordJar jar;

    private WelcomeScreenChannel() {}

    public Channel channel() {
        if (channel == null) channel = jar.getChannelById(channelId);
        return channel;
    }

    public String description() {
        return description;
    }

    public String emojiId() {
        return emojiId;
    }

    public String emojiName() {
        return emojiName;
    }

}
