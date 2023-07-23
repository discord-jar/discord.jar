package com.seailz.discordjar.events.model.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.utils.json.SJSONObject;

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
    public MessageEvent(DiscordJar bot, long sequence, SJSONObject data) {
        super(bot, sequence, data);
    }

}
