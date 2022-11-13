package com.seailz.javadiscordwrapper.model.channel.utils;

import java.util.EnumSet;

/**
 * Represents the flags of a channel
 */
public enum ChannelFlags {

    PINNED(1), // 	   this thread is pinned to the top of its parent GUILD_FORUM channel
    REQUIRE_TAG(4); // whether a tag is required to be specified when creating a thread in a GUILD_FORUM channel. Tags are specified in the applied_tags field.


    private int id;

    ChannelFlags(int id) {
        this.id = id;
    }

    public int getLeftShiftId() {
        return 1 << id;
    }

    public static EnumSet<ChannelFlags> getChannelFlagsByInt(int flags) {
        EnumSet<ChannelFlags> set = EnumSet.noneOf(ChannelFlags.class);
        if (flags == 0)
            return set;
        for (ChannelFlags flag : ChannelFlags.values())
        {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }
}
