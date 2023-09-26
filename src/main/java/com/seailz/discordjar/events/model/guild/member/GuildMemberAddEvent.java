package com.seailz.discordjar.events.model.guild.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.guild.GuildEvent;
import com.seailz.discordjar.model.guild.Member;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when a user joins a guild.
 * <br><B>Requires the {@code GUILD_MEMBERS} intent.</B>
 *
 * @author Seailz
 * @see Member
 * @since 1.0
 */
public class GuildMemberAddEvent extends GuildEvent {
    public GuildMemberAddEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the {@link Member} that joined the guild.
     */
    public Member getMember() {
        return Member.decompile(getJson().getJSONObject("d"), getBot(), getJson().getJSONObject("d").getString("guild_id"),
                getBot().getGuildById(getJson().getJSONObject("d").getString("guild_id")));
    }
}
