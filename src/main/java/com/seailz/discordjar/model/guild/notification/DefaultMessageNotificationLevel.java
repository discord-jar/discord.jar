package com.seailz.discordjar.model.guild.notification;

/**
 * Represents the default message notification level of a guild
 * This identifies when a user will be pinged for a message.
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/resources/guild#guild-object-default-message-notification-level">Default Message Notification Level</a>
 * @since 1.0
 */
public enum DefaultMessageNotificationLevel {

    // members will receive notifications for all messages by default
    ALL_MESSAGES(1),
    // members will receive notifications only for messages that @mention them by default
    ONLY_MENTIONS(2);

    private final int code;

    DefaultMessageNotificationLevel(int code) {
        this.code = code;
    }

    public static DefaultMessageNotificationLevel getDefaultMessageNotificationLevel(int code) {
        for (DefaultMessageNotificationLevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}
