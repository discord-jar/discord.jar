package com.seailz.javadiscordwrapper.model.permission;

public enum OverwriteType {
    ROLE(0),
    MEMBER(1);

    private final int code;

    OverwriteType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OverwriteType fromCode(int code) {
        for (OverwriteType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
