package com.seailz.discordjv.events.model.guild;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.model.guild.Guild;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * This event fires when the bot joins a guild.
 * This will also fire multiple times when the gateway connection is initiated providing the client with a list of guilds
 *
 * @author Seailz
 * @see com.seailz.discordjv.events.DiscordListener
 * @since 1.0
 */
public class GuildCreateEvent extends Event {
    public GuildCreateEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    @NotNull
    public Guild getGuild() {
        return Guild.decompile(getJson().getJSONObject("d"), getBot());
    }
}
