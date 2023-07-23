package com.seailz.discordjar.events.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

public class ChannelEvent extends Event {
    public ChannelEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }

    public Channel getChannel() {
        return Channel.decompile(getJson(), getBot());
    }
}
