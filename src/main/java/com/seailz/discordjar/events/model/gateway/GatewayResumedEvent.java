package com.seailz.discordjar.events.model.gateway;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * This event fires when the gateway connection resumes.
 * This can usually be left alone and isn't needed to be listened for in most cases.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.DiscordListener
 * @since 1.0
 */
public class GatewayResumedEvent extends Event {
    public GatewayResumedEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }
}
