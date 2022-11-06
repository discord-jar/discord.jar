package com.seailz.javadiscordwrapper.events.model;

import com.seailz.javadiscordwrapper.DiscordJv;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public class Event {

    private final DiscordJv bot;
    private final long sequence;
    private final JSONObject json;

    public Event(@NonNull DiscordJv bot, @NonNull long sequence, @NonNull JSONObject data) {
        this.bot = bot;
        this.sequence = sequence;
        this.json = data;
    }

    @NonNull
    public DiscordJv getBot() {
        return bot;
    }

    @NonNull
    public long getSequence() {
        return sequence;
    }

    @NonNull
    public JSONObject getJson() {
        return json;
    }

}
