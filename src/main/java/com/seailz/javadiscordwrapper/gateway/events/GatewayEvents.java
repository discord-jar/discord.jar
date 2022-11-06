package com.seailz.javadiscordwrapper.gateway.events;

/**
 * Represents a message sent from the Discord gateway to the client
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.gateway.GatewayFactory
 */
public enum GatewayEvents {

    HELLO(10),
    HEARTBEAT_ACK(11),
    HEARTBEAT_REQUEST(1),
    DISPATCHED(0),
    RECONNECT(7),
    INVALID_SESSION(9),
    ;

    private int opCode;

    GatewayEvents(int opCode) {
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }

    public static GatewayEvents getEvent(int opCode) {
        for(GatewayEvents event : values()) {
            if(event.getOpCode() == opCode) {
                return event;
            }
        }
        return null;
    }

}
