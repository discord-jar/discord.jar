package com.seailz.javadiscordwrapper.model.message.activity;

public enum MessageActivityType {

    JOIN(1),
    SPECTATE(2),
    LISTEN(3),
    JOIN_REQUEST(5);

    private int code;

    MessageActivityType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MessageActivityType fromCode(int code) {
        for (MessageActivityType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
