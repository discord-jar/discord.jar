package com.seailz.discordjar.model.interaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IntegrationType {

    GUILD_INSTALL(0),
    USER_INSTALL(1),
    UNKNOWN(-1);

    private final int code;

    public static IntegrationType fromCode(int code) {
        for (IntegrationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
