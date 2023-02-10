package com.seailz.discordjar.action.guild;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Action for retrieving user guilds. Read {@link DiscordJar#getGuilds()} for more information.
 * This is an internal class, that is given to the user. It is highly recommended that you don't make your own instance of this class.
 *
 * @author Seailz
 * @since  1.0
 * @see    DiscordJar#getGuilds()
 */
public class GetCurrentUserGuildsAction {

    private String before;
    private String after;
    private int limit;
    private final DiscordJar discordJar;

    public GetCurrentUserGuildsAction(DiscordJar discordJar) {
        this.before = null;
        this.after = null;
        this.limit = 200;
        this.discordJar = discordJar;
    }

    public GetCurrentUserGuildsAction before(String before) {
        Checker.isSnowflake(before, "Before must be a snowflake");
        this.before = before;
        return this;
    }

    public GetCurrentUserGuildsAction after(String after) {
        Checker.isSnowflake(after, "After must be a snowflake");
        this.after = after;
        return this;
    }

    public GetCurrentUserGuildsAction limit(int limit) {
        Checker.check(limit < 200, "Limit must be less than 200");
        Checker.check(limit > 0, "Limit must be greater than 0");
        this.limit = limit;
        return this;
    }

    public String before() {
        return before;
    }

    public String after() {
        return after;
    }

    public int limit() {
        return limit;
    }

    /**
     * Retrieves up to 200 guilds the bot is in.
     * <br>If you want to retrieve more guilds than that, you need to specify the last guild id in the <b>after</b> parameter.
     *<p>
     * Please be aware of the fact that this method is rate limited quite heavily.
     * <br>It is recommended that only smaller bots use this method.
     *<p>
     * If you need to retrieve a (possibly inaccurate) list of guilds as a larger bot, use {@link DiscordJar#getGuildCache()} instead.
     * <br>All guilds retrieved from this method will be cached.
     *
     * @return {@link List A list of}  <b>partial</b> {@link Guild guilds}
     */
    public List<Guild> run() {
        String url = URLS.GET.GUILDS.GET_CURRENT_USER_GUILDS;
        if (before != null || after != null || limit != 0) {
            url += "?";
            if (before != null)
                url += "before=" + before + "&";
            if (after != null)
                url += "after=" + after + "&";
            if (limit != 0)
                url += "limit=" + limit + "&";
        }

        DiscordResponse response = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                url,
                discordJar,
                URLS.GET.GUILDS.GET_CURRENT_USER_GUILDS,
                RequestMethod.GET
        ).invoke();

        List<Guild> returnGuilds = new ArrayList<>();
        response.arr().forEach(guild -> returnGuilds.add(Guild.decompile((JSONObject) guild, discordJar)));

        returnGuilds.forEach(g -> discordJar.getGuildCache().cache(g));
        return returnGuilds;
    }



}
