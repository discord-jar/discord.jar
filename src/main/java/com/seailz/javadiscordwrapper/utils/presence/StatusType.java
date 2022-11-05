package com.seailz.javadiscordwrapper.utils.presence;

public enum StatusType {

    ONLINE("online"),
    IDLE("idle"),
    DO_NOT_DISTURB("dnd"),
    INVISIBLE("invisible"),
    OFFLINE("offline");

    private String code;

    StatusType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
