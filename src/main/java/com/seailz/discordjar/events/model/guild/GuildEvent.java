package com.seailz.discordjar.events.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.guild.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.seailz.discordjar.utils.json.SJSONObject;

public class GuildEvent extends Event {

    public GuildEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        super(bot, sequence, data);
    }

    @Nullable
    public Guild getGuild() {
        return getBot().getGuildById(getJson().getJSONObject("d").getString("guild_id"));
    }

}
