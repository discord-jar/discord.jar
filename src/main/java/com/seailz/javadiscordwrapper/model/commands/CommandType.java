package com.seailz.javadiscordwrapper.model.commands;

public enum CommandType {
    SLASH_COMMAND(1),
    USER(2),
    MESSAGE(3);

    private int code;

    CommandType(int code) {this.code = code;}

    public int getCode() {
        return code;
    }

    public static CommandType fromCode(int code) {
        for (CommandType type : CommandType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
