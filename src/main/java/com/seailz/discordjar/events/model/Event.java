package com.seailz.discordjar.events.model;

import com.seailz.discordjar.DiscordJar;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents an event that is fired by the Discord API
 * An event can be implemented into your code by extending the {@link com.seailz.discordjar.events.DiscordListener} class
 * and overriding the event you want to listen for.
 * <p>
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class Event {

    private String name;
    private final DiscordJar bot;
    private final long sequence;
    private final JSONObject json;

    public Event(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        this.bot = bot;
        this.sequence = sequence;
        this.json = data;
    }

    /**
     * Returns the literal name of the event - e.g. {@code MESSAGE_CREATE}
     */
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public DiscordJar getBot() {
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
