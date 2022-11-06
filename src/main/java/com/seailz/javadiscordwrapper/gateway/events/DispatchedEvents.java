package com.seailz.javadiscordwrapper.gateway.events;

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

    READY,
    GUILD_CREATE,
    RESUMED,
    MESSAGE_CREATE,
    ;

    public static DispatchedEvents getEventByName(String name) {
        for (DispatchedEvents event : values()) {
            if (event.name().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }

}
