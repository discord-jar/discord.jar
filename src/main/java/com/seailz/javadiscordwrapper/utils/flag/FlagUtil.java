package com.seailz.javadiscordwrapper.utils.flag;

import com.seailz.javadiscordwrapper.model.ApplicationFlag;
import com.seailz.javadiscordwrapper.model.Flag;

import java.util.EnumSet;

public class FlagUtil {

    public static EnumSet<Flag> getFlagsByInt(int flags) {
        EnumSet<Flag> set = EnumSet.noneOf(Flag.class);
        if (flags == 0)
            return set;
        for (Flag flag : Flag.values())
        {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

    public static EnumSet<ApplicationFlag> getApplicationFlagsByInt(int flags) {
        EnumSet<ApplicationFlag> set = EnumSet.noneOf(ApplicationFlag.class);
        if (flags == 0)
            return set;
        for (ApplicationFlag flag : ApplicationFlag.values())
        {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

}
