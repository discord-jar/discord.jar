package com.seailz.javadiscordwrapper.events.model;

import com.seailz.javadiscordwrapper.DiscordJv;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents an event that is fired by the Discord API
 * An event can be implemented into your code by extending the {@link com.seailz.javadiscordwrapper.events.DiscordListener} class
 * and overriding the event you want to listen for.
 *
 * This is an internal class and should not be used by the end user.
 * @see    com.seailz.javadiscordwrapper.events.DiscordListener
 * @author Seailz
 * @since  1.0
 */
public class Event {

    private final DiscordJv bot;
    private final long sequence;
    private final JSONObject json;

    public Event(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        this.bot = bot;
        this.sequence = sequence;
        this.json = data;
    }

    @NotNull
    public DiscordJv getBot() {
        return bot;
    }

    public long getSequence() {
        return sequence;
    }

    @NotNull
    public JSONObject getJson() {
        return json;
    }

}
