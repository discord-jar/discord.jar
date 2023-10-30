package com.seailz.discordjar.events.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.guild.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * This event fires when the bot joins a guild.
 * This will also fire multiple times when the gateway connection is initiated providing the client with a list of guilds
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class GuildCreateEvent extends Event {
    public GuildCreateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public Guild getGuild() {
        return Guild.decompile(getJson().getJSONObject("d"), getBot(), false);
    }
}
