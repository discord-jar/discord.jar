package com.seailz.javadiscordwrapper.model;

/**
 * Represents a flag on a user's account.
 * This is used to determine if a user has a certain feature enabled or not, and USUALLY represents "badges" on a user's account.
 * @author Seailz
 * @since 1.0
 */
public enum Flag {

    // User is a staff member
    STAFF(0),
    // User is an owner of a partnered server
    PARTNER(1),
    // User is a hypesquad events member
    HYPESQUAD(2),
    // User is bug hunter lvl1
    BUG_HUNTER_LEVEL_1(3),
    // User is bug hunter lvl2
    BUG_HUNTER_LEVEL_2(14),
    // User is a hypesquad house 1 member
    HYPESQUAD_BRAVERY(6),
    // User is a hypesquad house 2 member
    HYPESQUAD_BRILLIANCE(7),
    // User is a hypesquad house 3 member
    HYPESQUAD_BALANCE(8),
    // User was an early supporter for Discord Nitro
    NITRO_EARLY_SUPPORTER(9),
    // User is part of a developer team
    TEAM_PSEUDO_USER(10),
    // User is a verified bot
    VERIFIED_BOT(16),
    // User is a verified bot developer
    EARLY_VERIFIED_BOT_DEVELOPER(17),
    // User is a discord certified moderator
    DISCORD_CERTIFIED_MODERATOR(18),
    // User only uses HTTP interactions and is shown in the only member list
    BOT_HTTP_INTERACTIONS(19),
    ;

    private int id;

    Flag(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }
}
