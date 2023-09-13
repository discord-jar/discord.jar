package com.seailz.discordjar.model.guild.verification;

/**
 * Represents the verification level of a guild
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/resources/guild#guild-object-verification-level">Verification Level</a>
 * @since 1.0
 */
public enum VerificationLevel {

    // unrestricted
    NONE(0),
    // must have verified email on account
    LOW(1),
    // must be registered on Discord for longer than 5 minutes
    MEDIUM(2),
    // must be a member of the server for longer than 10 minutes
    HIGH(3),
    // must have a verified phone number
    VERY_HIGH(4);

    private int code;

    VerificationLevel(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static VerificationLevel getVerificationLevel(int code) {
        for (VerificationLevel level : values()) {
            if (level.getCode() == code) {
                return level;
            }
        }
        return null;
    }

}
