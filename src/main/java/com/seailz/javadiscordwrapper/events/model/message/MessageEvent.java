package com.seailz.javadiscordwrapper.events.model.message;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.Event;
import org.json.JSONObject;

public class MessageEvent extends Event {
    public MessageEvent(DiscordJv bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }
}
