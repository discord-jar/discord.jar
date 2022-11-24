package com.seailz.discordjv.model.channel.utils;

/**
 * Represents the type of a channel.
 *
 * @author Seailz
 */
public enum ChannelType {

    GUILD_TEXT(0, "Text Channel", true, true),
    DM(1, "DM", true, false),
    GUILD_VOICE(2, "Voice Channel", false, true),
    GROUP_DM(3, "Group DM", true, false),
    GUILD_CATEGORY(4, "Category", false, true),
    GUILD_ANNOUNCEMENT(5, "Announcement", true, true),
    ANNOUNCEMENT_THREAD(10, "Announcement Thread", true, true),
    PUBLIC_THREAD(11, "Public Thread", true, true),
    PRIVATE_THREAD(12, "Private Thread", true, true),
    GUILD_STAGE_VOICE(13, "Stage Channel", false, true),
    GUILD_DIRECTORY(14, "Student Hub Directory", false, true),
    GUILD_FORUM(15, " Forum", false, true),

    ;
    private final int code;
    private final String name;

    ChannelType(int code, String name, boolean isMessage, boolean isGuild) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ChannelType fromCode(int code) {
        for (ChannelType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}
