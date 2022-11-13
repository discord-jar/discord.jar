package com.seailz.javadiscordwrapper.events.model.gateway;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.Event;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * This event fires when the gateway connection resumes.
 * This can usually be left alone and isn't needed to be listened for in most cases.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.events.DiscordListener
 */
public class GatewayResumedEvent extends Event {
    public GatewayResumedEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }
}
