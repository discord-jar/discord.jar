package com.seailz.discordjar.command.permissions;

public enum ApplicationCommandPermissionType {

    ROLE(1),
    USER(2),
    CHANNEL(3);

    private final int code;

    ApplicationCommandPermissionType(int code) {
        this.code = code;
    }

    public static ApplicationCommandPermissionType getType(int code) {
        for (ApplicationCommandPermissionType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}
