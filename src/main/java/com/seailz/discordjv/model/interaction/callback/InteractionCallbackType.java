package com.seailz.discordjv.model.interaction.callback;

/**
 * Reperesents the type of a callback sent to an interaction.
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.interaction.Interaction
 * @since 1.0
 */
public enum InteractionCallbackType {

    // ACK a Ping
    PONG(1),
    // respond to an interaction with a message
    CHANNEL_MESSAGE_WITH_SOURCE(4),
    // 	ACK an interaction and edit a response later, the user sees a loading state
    DEFERRED_CHANNEL_MESSAGE_WITH_SOURCE(5),
    // 	for components, ACK an interaction and edit the original message later; the user does not see a loading state
    DEFERRED_UPDATE_MESSAGE(6),
    // for components, edit the message the component was attached to
    UPDATE_MESSAGE(7),
    // respond to an autocomplete interaction with suggested choices
    APPLICATION_COMMAND_AUTOCOMPLETE_RESULT(8),
    // 	respond to an interaction with a popup modal
    MODAL(9),
    UNKNOWN(-1);

    private final int code;

    InteractionCallbackType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static InteractionCallbackType fromCode(int code) {
        for (InteractionCallbackType value : values()) {
            if (value.getCode() == code)
                return value;
        }
        return UNKNOWN;
    }
}
