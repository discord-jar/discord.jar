package com.seailz.discordjar.model.status.activity;

import java.util.EnumSet;

/**
 * Represents a flag on an activity
 *
 * @author Seailz
 * @since 1.0
 */
public enum ActivityFlags {

    INSTANCE(0),
    JOIN(1),
    SPECTATE(2),
    JOIN_REQUEST(3),
    SYNC(4),
    PLAY(5),
    PARTY_PRIVACY_FRIENDS(6),
    PARTY_PRIVACY_VOICE_CHANNEL(7),
    EMBEDDED(8);

    private final int id;

    ActivityFlags(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }

    public static EnumSet<ActivityFlags> fromInt(int i) {
        EnumSet<ActivityFlags> set = EnumSet.noneOf(ActivityFlags.class);
        for (ActivityFlags flag : ActivityFlags.values()) {
            if ((i & flag.getLeftShiftId()) == flag.getLeftShiftId()) {
                set.add(flag);
            }
        }
        return set;
    }
}
