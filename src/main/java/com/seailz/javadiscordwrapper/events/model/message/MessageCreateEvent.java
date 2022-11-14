package com.seailz.javadiscordwrapper.events.model.message;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.message.Message;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * This event will fire when a message is sent/created.
 * You can listen for this event by extending the {@link com.seailz.javadiscordwrapper.events.DiscordListener} class
 * and overriding the {@link com.seailz.javadiscordwrapper.events.DiscordListener#onMessageReceived(MessageCreateEvent)} method.
 *
 * @see    com.seailz.javadiscordwrapper.events.DiscordListener
 * @author Seailz
 * @since  1.0
 */
public class MessageCreateEvent extends MessageEvent {

    /**
     * Creates a new MessageCreateEvent
     * @param bot      The current bot instance
     * @param sequence The sequence number of the event
     * @param data     The data of the event
     */
    public MessageCreateEvent(DiscordJv bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
        System.out.println(data.toString());
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
     * This shouldn't return null.
     * @return A {@link Guild} object
     */
    @NotNull
    public Guild getGuild() {
        return getBot().getGuildCache().getById((getJson().getJSONObject("d").getString("guild_id")));
    }
}
