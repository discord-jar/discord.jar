package com.seailz.discordjar.events.model.general;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.application.Application;
import com.seailz.discordjar.model.guild.UnavailableGuild;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fires when Discord is ready to start sending events.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class ReadyEvent extends Event {
    public ReadyEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the bot user that is logged in
     */
    @NotNull
    public User getUser() {
        return (User) ModelDecoder.decodeObject(getJson().getJSONObject("d").getJSONObject("user"), User.class, getBot());
    }

    @NotNull
    public String getSessionId() {
        return getJson().getJSONObject("d").getString("session_id");
    }

    /*
    Sharding is not currently supported yet.
    TODO: Add sharding support
    @NotNull
    public String getShard() {
        return getJson().getJSONObject("d").getString("");
    } **/

    @NotNull
    public List<UnavailableGuild> getGuilds() {
        List<UnavailableGuild> guilds = new ArrayList<>();
        for (Object guild : getJson().getJSONObject("d").getJSONArray("guilds")) {
            guilds.add(UnavailableGuild.decompile((JSONObject) guild, getBot()));
        }
        return guilds;
    }

    @NotNull
    public String getResumeGatewayUrl() {
        return getJson().getJSONObject("d").getString("url");
    }

    @NotNull
    public Application getApplication() {
        return (Application) ModelDecoder.decodeObject(getJson().getJSONObject("d").getJSONObject("application"), Application.class, getBot());
    }


}
