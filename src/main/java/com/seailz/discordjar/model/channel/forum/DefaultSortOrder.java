package com.seailz.discordjar.model.channel.forum;

public enum DefaultSortOrder {
    LATEST(0),
    CREATION(1),
    UNKNOWN(-1);

    private final int code;

    DefaultSortOrder(int code) {
        this.code = code;
    }

    public static DefaultSortOrder fromCode(int code) {
        for (DefaultSortOrder value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }
}
