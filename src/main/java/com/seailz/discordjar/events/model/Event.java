package com.seailz.discordjar.events.model;

import com.seailz.discordjar.DiscordJar;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONObject;

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

    private final DiscordJar bot;
    private final long sequence;
    private final SJSONObject json;

    public Event(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
        this.bot = bot;
        this.sequence = sequence;
        this.json = data;
    }

    @NotNull
    public DiscordJar getBot() {
        return bot;
    }

    public long getSequence() {
        return sequence;
    }

    @NotNull
    public SJSONObject getJson() {
        return json;
    }

}
