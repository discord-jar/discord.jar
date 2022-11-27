package com.seailz.discordjv.command;

public enum CommandOptionType {

    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3),
    // Any integer between -2^53 and 2^53
    INTEGER(4),
    BOOLEAN(5),
    USER(6),
    // Includes all channel types + categories
    CHANNEL(7),
    ROLE(8),
    // Includes users and roles
    MENTIONABLE(9),
    // 	Any double between -2^53 and 2^53
    NUMBER(10),
    ATTACHMENT(11);

    private final int code;

    CommandOptionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CommandOptionType fromCode(int code) {
        for (CommandOptionType type : CommandOptionType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
