package com.seailz.discordjar.events.model.guild.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.guild.GuildEvent;
import com.seailz.discordjar.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when a user is removed from a guild.
 * This includes:
 * <ul>
 *     <li>Leaving</li>
 *     <li>Getting Kicked</li>
 *     <li>Getting Banned</li>
 * </ul>
 *
 * <b>Requires the {@code GUILD_MEMBERS} intent</b>.
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.discordjar.model.guild.Member Member
 */
public class GuildMemberRemoveEvent extends GuildEvent {
    public GuildMemberRemoveEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the {@link User} that was removed from the guild.
     * @return {@link User} object.
     */
    public User getUser() {
        return User.decompile(getJson().getJSONObject("d").getJSONObject("user"), getBot());
    }

    /**
     * Returns the guild id of the guild the user was removed from.
     */
    public String getGuildId() {
        return getJson().getJSONObject("d").getString("guild_id");
    }
}
