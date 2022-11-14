package com.seailz.javadiscordwrapper.model.interaction;

/**
 * Represents the type of interaction
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public enum InteractionType {

    PING(1),
    APPLICATION_COMMAND(2),
    MESSAGE_COMPONENT(3),
    APPLICATION_COMMAND_AUTOCOMPLETE(4),
    MODAL_SUBMIT(5),

    ;

    private int code;

    InteractionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static InteractionType getType(int code) {
        for(InteractionType type : values()) {
            if(type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
