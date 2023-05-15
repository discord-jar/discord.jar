package com.seailz.discordjar.events.model.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * This event will fire when a message is sent/created.
 * You can listen for this event by extending the {@link com.seailz.discordjar.events.DiscordListener} class
 * and overriding the {@link com.seailz.discordjar.events.DiscordListener#onMessageReceived(MessageCreateEvent)} method.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class MessageCreateEvent extends MessageEvent {

    /**
     * Creates a new MessageCreateEvent
     *
     * @param bot      The current bot instance
     * @param sequence The sequence number of the event
     * @param data     The data of the event
     */
    public MessageCreateEvent(DiscordJar bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the message that was sent/created
     */
    @NotNull
    public Message getMessage() {
        return Message.decompile(getJson().getJSONObject("d"), getBot());
    }

    /**
     * The {@link Guild} the message was sent in
     * This will return null if the message was sent in a DM.
     *
     * @return A {@link Guild} object
     */
    @Nullable
    public Guild getGuild() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        if (!getJson().getJSONObject("d").has("guild_id") || getJson().getJSONObject("d").isNull("guild_id")) return null;
        return getBot().getGuildCache().getById((getJson().getJSONObject("d").getString("guild_id")));
    }
}
