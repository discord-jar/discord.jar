package com.seailz.discordjv.events.model.message;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import org.json.JSONObject;

/**
 * Represents a message event that is fired by the Discord API
 * This class is currently not actually functional but may have a use in the future.
 * <p>
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @since 1.0
 */
class MessageEvent extends Event {
    public MessageEvent(DiscordJv bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }

}
