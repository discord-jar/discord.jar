package com.seailz.discordjv.model.application;

/**
 * Represents an intent
 *
 * @author Seailz
 * @since 1.0
 */
public enum Intent {

    GUILDS(0, false),
    GUILD_MEMBERS(1, true),
    GUILD_BANS(2, false),
    GUILD_EMOJIS_AND_STICKERS(3, false),
    GUILD_INTEGRATIONS(4, false),
    GUILD_WEBHOOKS(5, false),
    GUILD_INVITES(6, false),
    GUILD_VOICE_STATES(7, false),
    GUILD_PRESENCES(8, true),
    GUILD_MESSAGES(9, false),
    GUILD_MESSAGE_REACTIONS(10, false),
    GUILD_MESSAGE_TYPING(11, false),
    DIRECT_MESSAGES(12, false),
    DIRECT_MESSAGE_REACTIONS(13, false),
    DIRECT_MESSAGE_TYPING(14, false),
    MESSAGE_CONTENT(15, true),
    GUILD_SCHEDULED_EVENTS(16, false),
    AUTO_MOD_CONFIG(20, false),
    AUTO_MOD_EXECUTION(21, false),
    // Represents all intents, excluding privileged intents
    ALL(22, false);

    private final int id;
    private final boolean privileged;

    Intent(int id, boolean privileged) {
        this.id = id;
        this.privileged = privileged;
    }

    public int getLeftShiftId() {
        if (this == ALL) return 3276799;
        return 1 << id;
    }

    public boolean isPrivileged() {
        return privileged;
    }

}
