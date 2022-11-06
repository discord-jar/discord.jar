package com.seailz.javadiscordwrapper.model.message;

public enum MessageType {

    DEFAULT(0, true),
    RECIPIENT_ADD(1, false),
    RECIPIENT_REMOVE(2, false),
    CALL(3, false),
    CHANNEL_NAME_CHANGE(4, false),
    CHANNEL_ICON_CHANGE(5, false),
    CHANNEL_PINNED_MESSAGE(6, true),
    USER_JOIN(7, true),
    GUILD_BOOST(8, true),
    GUILD_BOOST_TIER_1(9, true),
    GUILD_BOOST_TIER_2(10, true),
    GUILD_BOOST_TIER_3(11, true),
    CHANNEL_FOLLOW_ADD(12, true),
    GUILD_DISCOVERY_DISQUALIFIED(14, false),
    GUILD_DISCOVERY_REQUALIFIED(15, false),
    GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16, false),
    GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17, false),
    THREAD_CREATED(18, true),
    REPLY(19, true),
    APPLICATION_COMMAND(20, true),
    THREAD_STARTER_MESSAGE(21, true),
    GUILD_INVITE_REMINDER(22, true),
    CONTEXT_MENU_COMMAND(23, true),
    AUTO_MOD_ACTION(24, true),

    ;


    private boolean isDeletable;
    private int code;

    MessageType(int code, boolean deletable) {
        this.isDeletable = deletable;
        this.code = code;
    }

    public boolean isDeletable() {
        return isDeletable;
    }

    public int getCode() {
        return code;
    }

    public static MessageType fromCode(int code) {
        for (MessageType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
