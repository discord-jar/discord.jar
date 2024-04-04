package com.seailz.discordjar.model.interaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InteractionContextType {

    GUILD(0),
    BOT_DM(1),
    PRIVATE_CHANNEL(2),
    UNKNOWN(-1);

    private final int code;

    public static InteractionContextType fromCode(int code) {
        for (InteractionContextType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

}
