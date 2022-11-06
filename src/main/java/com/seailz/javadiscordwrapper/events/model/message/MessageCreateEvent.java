package com.seailz.javadiscordwrapper.events.model.message;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.message.Message;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

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
    }

    /**
     * Returns the message that was sent/created
     */
    @NotNull
    public Message getMessage() {
        return Message.decompile(getJson().getJSONObject("d"));
    }

    /**
     * The guild the message was sent in
     * This shouldn't return null.
     * @return The guild the message was sent in
     */
    @NotNull
    public Guild getGuild() {
        AtomicReference<Guild> returnValue = new AtomicReference<>();
        getBot().getGuildCache().getCache().forEach(guild -> {
            if (guild.id().equals(getJson().getString("guild_id"))) {
                returnValue.set(guild);
            }
        });
        // TODO: if guild isn't in cache, get it from the api

        return returnValue.get();
    }
}
