package com.seailz.javadiscordwrapper.model.application;

/**
 * Represents a flag on an application's account.
 * This is used to determine if an application has a certain feature enabled or not.
 * @author Seailz
 * @since 1.0
 */
public enum ApplicationFlag {

    // Intent required for bots in 100 or more servers to receive presence_update events
    GATEWAY_PRESENCE(12),
    // Intent required for bots in under 100 servers to receive presence_update events, found in Bot Settings
    GATEWAY_PRESENCE_LIMITED(13),
    // Intent required for bots in 100 or more servers to receive member-related events like guild_member_add. See list of member-related events under GUILD_MEMBERS
    GATEWAY_GUILD_MEMBERS(14),
    // Intent required for bots in under 100 servers to receive member-related events like guild_member_add, found in Bot Settings. See list of member-related events under GUILD_MEMBERS
    GATEWAY_GUILD_MEMBERS_LIMITED(15),
    // Indicates unusual growth of an app that prevents verification
    VERIFICATION_PENDING_GUILD_LIMIT(16),
    // Indicates if an app is embedded within the Discord client (currently unavailable publicly)
    EMBEDDED(17),
    // Intent required for bots in 100 or more servers to receive message content
    GATEWAY_MESSAGE_CONTENT(18),
    // Intent required for bots in under 100 servers to receive message content, found in Bot Settings
    GATEWAY_MESSAGE_CONTENT_LIMITED(19),
    // Indicates if an app has registered global application commands
    APPLICATION_COMMANDS_BADE(23),
    // considered an "active" bot, can be used for getting the "Active Developer" badge
    ACTIVE_BOT(24),
    ;

    private int id;

    ApplicationFlag(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }
}
