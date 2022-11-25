package com.seailz.discordjv.model.channel.forum;

public enum DefaultSortOrder {
    LATEST(0),
    CREATION(1),
    ;

    private final int code;

    DefaultSortOrder(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static DefaultSortOrder fromCode(int code) {
        for (DefaultSortOrder value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
