package com.seailz.javadiscordwrapper.model;

/**
 * Represents an intent
 * @author Seailz
 * @since 1.0
 */
public enum Intent {

    GUILDS(0),
    GUILD_MEMBERS(1),
    GUILD_BANS(2),
    GUILD_EMOJIS_AND_STICKERS(3),
    GUILD_INTEGRATIONS(4),
    GUILD_WEBHOOKS(5),
    GUILD_INVITES(6),
    GUILD_VOICE_STATES(7),
    GUILD_PRESENCES(8),
    GUILD_MESSAGES(9),
    GUILD_MESSAGE_REACTIONS(10),
    GUILD_MESSAGE_TYPING(11),
    DIRECT_MESSAGES(12),
    DIRECT_MESSAGE_REACTIONS(13),
    DIRECT_MESSAGE_TYPING(14),
    MESSAGE_CONTENT(15),
    GUILD_SCHEDULED_EVENTS(16),
    AUTO_MOD_CONFIG(20),
    AUTO_MOD_EXECUTION(21),
    // Represents all intents
    ALL(22)
    ;

    private int id;

    Intent(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        if (this == ALL) return 3276799;
        return 1 << id;
    }

}
