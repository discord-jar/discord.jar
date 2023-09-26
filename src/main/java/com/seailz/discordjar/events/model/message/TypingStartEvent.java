package com.seailz.discordjar.events.model.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * This event will fire when a user starts typing.
 * You can listen for this event by extending the {@link com.seailz.discordjar.events.DiscordListener} class
 * and overriding the {@link com.seailz.discordjar.events.DiscordListener#onTypingStart(TypingStartEvent)} method.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class TypingStartEvent extends Event {

    private final String channelId;
    private final String guildId;
    private final String userId;
    private final long timestamp;
    private Channel channel;
    private Guild guild;
    private User user;
    private Member member;

    public TypingStartEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
        JSONObject inner = data.getJSONObject("d");
        channelId = inner.getString("channel_id");
        guildId = inner.has("guild_id") && !inner.isNull("guild_id") ? inner.getString("guild_id") : null;
        userId = inner.getString("user_id");
        timestamp = inner.getLong("timestamp");
        if (inner.has("member") && !inner.isNull("member"))
            member = Member.decompile(inner.getJSONObject("member"), getBot(), guildId, getGuild());
    }

    /**
     * Returns the id of the channel that the user started typing in.
     */
    @NotNull
    public String getChannelId() {
        return channelId;
    }

    /**
     * Returns the {@link Channel} that the user started typing in.
     */
    @Nullable
    public Channel getChannel() {
        if (channel == null)
            channel = getBot().getChannelById(channelId);
        return channel;
    }

    /**
     * Returns the id of the guild that the user started typing in, or null if it was a DM.
     */
    @Nullable
    public String getGuildId() {
        return guildId;
    }

    /**
     * Returns the {@link Guild} that the user started typing in, or null if it was a DM.
     */
    @Nullable
    public Guild getGuild() {
        if (guild == null && guildId != null)
            guild = getBot().getGuildById(guildId);
        return guild;
    }

    /**
     * Returns the id of the user that started typing.
     */
    @NotNull
    public String getUserId() {
        return userId;
    }

    /**
     * Returns the {@link User} that started typing.
     */
    @Nullable
    public User getUser() {
        if (user == null)
            user = getBot().getUserById(userId);
        return user;
    }

    /**
     * Returns the unix timestamp of when the user started typing (in seconds).
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the {@link Member} that started typing, or null if it was a DM.
     */
    @Nullable
    public Member getMember() {
        Guild guild = getGuild();
        if (member == null && guild != null)
            member = guild.getMemberById(userId);
        return member;
    }
}
