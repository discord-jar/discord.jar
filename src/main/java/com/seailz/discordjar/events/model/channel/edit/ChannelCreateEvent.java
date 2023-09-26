package com.seailz.discordjar.events.model.channel.edit;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.channel.ChannelEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ChannelCreateEvent extends ChannelEvent {
    public ChannelCreateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }
}
