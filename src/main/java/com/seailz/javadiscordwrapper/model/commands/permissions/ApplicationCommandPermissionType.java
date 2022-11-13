package com.seailz.javadiscordwrapper.model.commands.permissions;

public enum ApplicationCommandPermissionType {

    ROLE(1),
    USER(2),
    CHANNEL(3);

    private int code;

    ApplicationCommandPermissionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ApplicationCommandPermissionType getType(int code) {
        for(ApplicationCommandPermissionType type : values()) {
            if(type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
