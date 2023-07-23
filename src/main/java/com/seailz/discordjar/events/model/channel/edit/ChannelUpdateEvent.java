package com.seailz.discordjar.events.model.channel.edit;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.channel.ChannelEvent;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONObject;

public class ChannelUpdateEvent extends ChannelEvent {
    public ChannelUpdateEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }
}
