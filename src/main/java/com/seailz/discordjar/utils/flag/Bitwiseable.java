package com.seailz.discordjar.utils.flag;

import java.util.EnumSet;
import java.util.List;

public interface Bitwiseable<T extends Enum<T> & Bitwiseable> {

    int getLeftShiftId();
    int id();

    default EnumSet<T> decode(long val) {
        return new BitwiseUtil<T>()
                .get(val, this.getClass());
    }

}
