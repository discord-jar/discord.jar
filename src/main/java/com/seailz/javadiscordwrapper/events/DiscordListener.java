package com.seailz.javadiscordwrapper.events;

import com.seailz.javadiscordwrapper.events.model.message.MessageCreateEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to listen for events that are fired by the Discord API.
 * This class can be extended to listen for events.
 * Once extended, you can override the methods you want to listen for.
 * Then, just register them in your main class.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.events.EventDispatcher
 * @see    com.seailz.javadiscordwrapper.events.annotation.EventMethod
 */
public abstract class DiscordListener {
    public void onMessageReceived(@NotNull MessageCreateEvent event) {}
}
