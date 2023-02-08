package com.seailz.discordjar.utils.flag;

import java.util.EnumSet;

/**
 * BitwiseUtil is a utility class for bitwise operations.
 * It allows you to retrieve things like {@link com.seailz.discordjar.model.user.UserFlag UserFlags} from a raw int.
 *
 * @author Seailz
 * @since  1.0
 * @param  <T> Enum to return, such as {@link com.seailz.discordjar.model.user.UserFlag UserFlag}
 */
public class BitwiseUtil<T extends Enum<T> & Bitwiseable> {

    public EnumSet<T> get(long flags, Class<T> clazz) {
        EnumSet<T> set = EnumSet.noneOf(clazz);
        if (flags == 0)
            return set;
        for (T flag : clazz.getEnumConstants()) {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

    public EnumSet<T> get(int flags, Class<T> clazz) {
        return get((long) flags, clazz);
    }

}
