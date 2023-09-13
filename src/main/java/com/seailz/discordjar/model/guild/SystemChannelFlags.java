package com.seailz.discordjar.model.guild;

import com.seailz.discordjar.utils.flag.Bitwiseable;

public enum SystemChannelFlags implements Bitwiseable {
    SUPPRESS_JOIN_NOTIFICATIONS(0),
    SUPPRESS_PREMIUM_SUBSCRIPTIONS(1),
    SUPPRESS_GUILD_REMINDER_NOTIFICATIONS(2),
    SUPPRESS_JOIN_NOTIFICATION_REPLIES(3),
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS(4),
    SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES(5)
    ;

    private final int id;

    SystemChannelFlags(int id) {
        this.id = id;
    }

    @Override
    public int getLeftShiftId() {
        return 1 << id;
    }

    @Override
    public int id() {
        return id;
    }
}
