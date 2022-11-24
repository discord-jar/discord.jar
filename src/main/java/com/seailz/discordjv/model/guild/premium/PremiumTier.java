package com.seailz.discordjv.model.guild.premium;

/**
 * Represents the premium tier of a guild.
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/resources/guild#guild-object-premium-tier">Premium Tier</a>
 * @since 1.0
 */
public enum PremiumTier {

    // guild has no nitro boost
    NONE(0),
    // guild has nitro boost level 1
    TIER_1(1),
    // guild has nitro boost level 2
    TIER_2(2),
    // guild has nitro boost level 3
    TIER_3(3);

    private final int code;

    PremiumTier(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PremiumTier getPremiumTier(int code) {
        for (PremiumTier tier : values()) {
            if (tier.getCode() == code) {
                return tier;
            }
        }
        return null;
    }

}
