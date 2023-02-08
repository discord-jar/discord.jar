package com.seailz.discordjar.command;

/**
 * The types a {@link Command} can be registered as.
 */
public enum CommandType {
    SLASH_COMMAND(1),
    USER(2),
    MESSAGE(3),
    UNKNOWN(-1);

    private final int code;

    CommandType(int code) {
        this.code = code;
    }

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
