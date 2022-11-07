package com.seailz.javadiscordwrapper.gateway.events;

import com.seailz.javadiscordwrapper.events.model.Event;
import com.seailz.javadiscordwrapper.events.model.command.CommandPermissionUpdateEvent;
import com.seailz.javadiscordwrapper.events.model.gateway.GatewayResumedEvent;
import com.seailz.javadiscordwrapper.events.model.general.ReadyEvent;
import com.seailz.javadiscordwrapper.events.model.guild.GuildCreateEvent;
import com.seailz.javadiscordwrapper.events.model.message.MessageCreateEvent;

/**
 * Represents a gateway event that is fired by the Discord API
 * These events are not things like "send heartbeat", "send identify", "hello", (which can be found here {@link GatewayEvents})
 * but a rather events that fall under the "dispatch" category.
 * These events are things like "message create", "message update", "message delete", etc.
 *
 * This is an internal class and should not be used by the end user.
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.gateway.events.GatewayEvents
 */
public enum DispatchedEvents {

    READY(ReadyEvent.class),
    GUILD_CREATE(GuildCreateEvent.class),
    RESUMED(GatewayResumedEvent.class),
    MESSAGE_CREATE(MessageCreateEvent.class),
    APPLICATION_COMMAND_PERMISSION_UPDATE(CommandPermissionUpdateEvent.class)
    ;

    private final Class<? extends Event> event;

    DispatchedEvents(Class<? extends Event> event) {
        this.event = event;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public static DispatchedEvents getEventByName(String name) {
        for (DispatchedEvents event : values()) {
            if (event.name().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }

}
