package com.seailz.discordjar.model.guild.mfa;

/**
 * Represents the MFA level of a guild.
 * This identifies if moderators need to have 2FA enabled on their account to perform moderation actions.
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/resources/guild#guild-object-mfa-level">MFA Level</a>
 * @since 1.0
 */
public enum MFALevel {

    // guild has no MFA/2FA requirement for moderation actions
    NONE(0),
    // guild has a 2FA requirement for moderation actions
    ELEVATED(1);

    private final int code;

    MFALevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MFALevel getMFALevel(int code) {
        for (MFALevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        return null;
    }

}
