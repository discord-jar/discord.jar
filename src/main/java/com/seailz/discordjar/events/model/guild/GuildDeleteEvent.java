package com.seailz.discordjar.events.model.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.guild.UnavailableGuild;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when the guild becomes or was already unavailable due to an outage, or when the user leaves or is removed from a guild.
 */
public class GuildDeleteEvent extends Event {

    private UnavailableGuild guild;

    public GuildDeleteEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
        guild = UnavailableGuild.decompile(getJson().getJSONObject("d"), getBot());
    }

    /**
     * Returns the {@link UnavailableGuild} that was deleted.
     * @return {@link UnavailableGuild} object.
     */
    public UnavailableGuild getGuild() {
        return guild;
    }

    /**
     * Returns true if the current user was removed from the guild.
     */
    public boolean wasRemoved() {
        return !getJson().getJSONObject("d").has("unavailable") || !getJson().getJSONObject("d").getBoolean("unavailable");
    }


}
