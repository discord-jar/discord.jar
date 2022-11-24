package com.seailz.discordjv.events.model.general;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.model.application.Application;
import com.seailz.discordjv.model.guild.UnavailableGuild;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fires when Discord is ready to start sending events.
 *
 * @author Seailz
 * @see com.seailz.discordjv.events.DiscordListener
 * @since 1.0
 */
public class ReadyEvent extends Event {
    public ReadyEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the bot user that is logged in
     */
    @NotNull
    public User getUser() {
        return User.decompile(getJson().getJSONObject("d").getJSONObject("user"), getBot());
    }

    @NotNull
    public String getSessionId() {
        return getJson().getJSONObject("d").getString("session_id");
    }

    /*
    Sharding is not currently supported yet.

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
        return Application.decompile(getJson().getJSONObject("d").getJSONObject("application"), getBot());
    }


}
