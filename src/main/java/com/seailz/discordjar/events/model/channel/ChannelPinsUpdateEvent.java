package com.seailz.discordjar.events.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.guild.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Dispatched when the pins for a channel are updated. This could be because:
 * <ul>
 *     <li>A message was pinned</li>
 *     <li>A message was unpinned</li>
 * </ul>
 * <p>It is not dispatched when a pinned message is deleted.</p>
 */
public class ChannelPinsUpdateEvent extends Event {

    private String guildId;
    private Guild guild;
    private String channelId;
    private Channel channel;
    private LocalDateTime lastPinTimestamp;


    public ChannelPinsUpdateEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);

        guildId = data.has("guild_id") ? data.getString("guild_id") : null;
        channelId = data.has("channel_id") ? data.getString("channel_id") : null;
        // last_pin_timestamp is a iso8601 timestamp, so we need to get a Date object from it
        if (data.has("last_pin_timestamp")) {
            String timestampString = data.getString("last_pin_timestamp");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            lastPinTimestamp = LocalDateTime.parse(timestampString, formatter);
        }
    }

    @Nullable
    public String getGuildId() {
        return guildId;
    }

    @Nullable
    public Guild getGuild() {
        if (guild == null && guildId != null)
            guild = getBot().getGuildById(guildId);
        return guild;
    }

    @NotNull
    public String getChannelId() {
        return channelId;
    }

    @Nullable
    public Channel getChannel() {
        if (channel == null)
            channel = getBot().getChannelById(channelId);
        return channel;
    }

    /**
     * The time at which the most recent pinned msg was pinned.
     */
    @Nullable
    public LocalDateTime getLastPinTimestamp() {
        return lastPinTimestamp;
    }
}
