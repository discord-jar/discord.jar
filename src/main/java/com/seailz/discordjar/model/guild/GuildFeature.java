package com.seailz.discordjar.model.guild;

import java.util.EnumSet;

public enum GuildFeature {

    // guild has access to set an animated guild banner image
    ANIMATED_BANNER,
    // guild has access to set an animated guild icon
    ANIMATED_ICON,
    // guild has set up auto moderation rules
    AUTO_MODERATION,
    // guild has access to set a guild banner image
    BANNER,
    // guild can enable welcome screen, Membership Screening, stage channels and discovery, and receives community updates
    COMMUNITY,
    // guild has enabled monetization
    CREATOR_MONETIZABLE_PROVISIONAL,
    // guild has enabled the role subscription promo page
    CREATOR_STORE_PAGE,
    // guild has been set as a support server on the App Directory
    DEVELOPER_SUPPORT_SERVER,
    // guild is able to be discovered in the directory
    DISCOVERABLE,
    // guild is able to be featured in the directory
    FEATURABLE,
    // guild has paused invites, preventing new users from joining
    INVITES_DISABLED,
    // guild has access to set an invite splash background
    INVITE_SPLASH,
    // guild has enabled Membership Screening
    MEMBER_VERIFICATION_GATE_ENABLED,
    // guild has enabled monetization
    MONETIZATION_ENABLED,
    // guild has increased custom sticker slots
    MORE_STICKERS,
    // guild has access to create announcement channels
    NEWS,
    // guild is partnered
    PARTNERED,
    // guild can be previewed before joining via Membership Screening or the directory
    PREVIEW_ENABLED,
    // guild has access to create private threads
    PRIVATE_THREADS,
    // guild is able to set role icons
    ROLE_ICONS,
    // guild has role subscriptions that can be purchased
    ROLE_SUBSCRIPTIONS_AVAILABLE_FOR_PURCHASE,
    // guild has enabled role subscriptions
    ROLE_SUBSCRIPTIONS_ENABLED,
    // guild has enabled ticketed events
    TICKETED_EVENTS_ENABLED,
    // guild has access to set a vanity URL
    VANITY_URL,
    // guild is verified
    VERIFIED,
    // guild has access to set 384kbps bitrate in voice (previously VIP voice servers)
    VIP_REGIONS,
    // guild has enabled the welcome screen
    WELCOME_SCREEN_ENABLED,
    // has perms v2
    APPLICATION_COMMAND_PERMISSIONS_V2,
    // guild has disabled alerts for join raids in the configured safety alerts channel
    RAID_ALERTS_DISABLED,

    UNKNOWN;

    public static EnumSet<GuildFeature> getGuildFeatures(String[] features) {
        EnumSet<GuildFeature> guildFeatures = EnumSet.noneOf(GuildFeature.class);
        if (features == null) return guildFeatures;
        for (String feature : features) {
            if (feature == null) continue;
            try {
                guildFeatures.add(GuildFeature.valueOf(feature.toUpperCase()));
            } catch (IllegalArgumentException e) {
                guildFeatures.add(GuildFeature.UNKNOWN);
            }
        }
        return guildFeatures;
    }

}
