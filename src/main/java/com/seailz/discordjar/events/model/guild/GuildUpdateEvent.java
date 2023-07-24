package com.seailz.discordjar.events.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.guild.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class GuildUpdateEvent extends Event {
    public GuildUpdateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public Guild getGuild() {
        return Guild.decompile(getJson().getJSONObject("d"), getBot());
    }
}
