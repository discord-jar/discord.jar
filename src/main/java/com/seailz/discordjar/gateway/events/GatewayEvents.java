package com.seailz.discordjar.gateway.events;

/**
 * Represents a message sent from the Discord gateway to the client
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjar.gateway.GatewayFactory
 * @since 1.0
 */
public enum GatewayEvents {

    HELLO(10),
    HEARTBEAT_ACK(11),
    HEARTBEAT_REQUEST(1),
    DISPATCHED(0),
    RECONNECT(7),
    INVALID_SESSION(9),
    ;

    private final int opCode;

    GatewayEvents(int opCode) {
        this.opCode = opCode;
    }

    public static GatewayEvents getEvent(int opCode) {
        for (GatewayEvents event : values()) {
            if (event.getOpCode() == opCode) {
                return event;
            }
        }
        return null;
    }

    public int getOpCode() {
        return opCode;
    }

}
