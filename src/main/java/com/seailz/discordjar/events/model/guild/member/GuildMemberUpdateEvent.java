package com.seailz.discordjar.events.model.guild.member;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.guild.GuildEvent;
import com.seailz.discordjar.model.guild.Member;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Sent when a guild member is updated.
 * <br>This will also fire when a user object of a guild member is updated.
 * <br><B>Requires the {@code GUILD_MEMBERS} intent.</B>
 */
public class GuildMemberUpdateEvent extends GuildEvent {
    public GuildMemberUpdateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * This will work, but is not totally recommended as not all fields of a member are included in this event.
     * <br>This may leave some values of the {@link Member} object as null.
     * <b>Use at your own risk!</b>.
     * <p>
     * <br>It's a good idea to check <a href="https://discord.com/developers/docs/topics/gateway-events#guild-member-update">the docs</a> to see what fields are included. If the
     * field you need isn't included, try using {@link #getMemberFresh()} instead. That will take more time, but will get the most up-to-date information.
     * @return Partial {@link Member} object
     */
    public Member getUpdatedMember() {
        return Member.decompile(getJson().getJSONObject("d"), getBot(), getJson().getJSONObject("d").getString("guild_id"),
                getGuild());
    }

    /**
     * Returns the updated member fresh from the API.
     * @return Updated {@link Member} object
     */
    public Member getMemberFresh() {
        return getGuild().getMemberById(getJson().getJSONObject("d").getJSONObject("user").getString("id"));
    }
}
