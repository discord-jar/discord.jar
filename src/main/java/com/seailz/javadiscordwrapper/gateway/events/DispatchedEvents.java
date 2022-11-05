package com.seailz.javadiscordwrapper.gateway.events;

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
