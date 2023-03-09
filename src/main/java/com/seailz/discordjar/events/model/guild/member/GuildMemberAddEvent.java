package com.seailz.discordjar.events.model.guild.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.guild.Member;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when a user joins a guild.
 * <br><B>Requires the {@code GUILD_MEMBERS} intent.</B>
 *
 * @author Seailz
 * @since  1.0
 * @see    Member
 */
public class GuildMemberAddEvent extends Event {
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
