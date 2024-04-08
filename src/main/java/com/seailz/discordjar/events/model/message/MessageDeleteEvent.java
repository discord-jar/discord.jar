package com.seailz.discordjar.events.model.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * This event will fire when a message is deleted.
 * You can listen for this event by extending the {@link com.seailz.discordjar.events.DiscordListener} class
 * and overriding the {@link com.seailz.discordjar.events.DiscordListener#onMessageDelete(MessageDeleteEvent)} method.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class MessageDeleteEvent extends MessageEvent {

    /**
     * Creates a new MessageDeleteEvent
     *
     * @param bot      The current bot instance
     * @param sequence The sequence number of the event
     * @param data     The data of the event
     */
    public MessageDeleteEvent(DiscordJar bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the ID of the message that was deleted
     */
    @NotNull
    public String getMessageId() {
        return getJson().getJSONObject("d").getString("id");
    }

    @NotNull
    public String getChannelId() {
        return getJson().getJSONObject("d").getString("channel_id");
    }

    @Nullable
    public String getGuildId() {
        if (!getJson().getJSONObject("d").has("guild_id") || getJson().getJSONObject("d").isNull("guild_id")) return null;
        return getJson().getJSONObject("d").getString("guild_id");
    }

    /**
     * The {@link Guild} the message was sent in
     * This will return null if the message was sent in a DM.
     *
     * @return A {@link Guild} object
     */
    @Nullable
    public Guild getGuild() {
        if (!getJson().getJSONObject("d").has("guild_id") || getJson().getJSONObject("d").isNull("guild_id")) return null;
        try {
            return getBot().getGuildCache().getById((getJson().getJSONObject("d").getString("guild_id")));
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }
}