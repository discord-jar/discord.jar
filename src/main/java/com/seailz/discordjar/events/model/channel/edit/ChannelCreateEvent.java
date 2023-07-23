package com.seailz.discordjar.events.model.channel.edit;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.channel.ChannelEvent;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;

public class ChannelCreateEvent extends ChannelEvent {
    public ChannelCreateEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }
}
