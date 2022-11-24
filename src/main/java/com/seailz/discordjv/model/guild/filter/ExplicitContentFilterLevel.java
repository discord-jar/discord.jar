package com.seailz.discordjv.model.guild.filter;

/**
 * Represents the explicit content filter level of a guild
 * This identifies the level of explicit content filtering in a guild.
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/resources/guild#guild-object-explicit-content-filter-level">Explicit Content Filter Level</a>
 * @since 1.0
 */
public enum ExplicitContentFilterLevel {

    // don't scan any messages
    DISABLED(0),
    // scan messages from members without a role
    MEMBERS_WITHOUT_ROLES(1),
    // scan all messages
    ALL_MEMBERS(2);

    private final int code;

    ExplicitContentFilterLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ExplicitContentFilterLevel getExplicitContentFilterLevel(int code) {
        for (ExplicitContentFilterLevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        return null;
    }

}
