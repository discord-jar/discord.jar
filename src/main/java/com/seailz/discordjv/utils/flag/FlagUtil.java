package com.seailz.discordjv.utils.flag;

import com.seailz.discordjv.model.application.ApplicationFlag;
import com.seailz.discordjv.model.user.UserFlag;

import java.util.EnumSet;

public class FlagUtil {

    public static EnumSet<UserFlag> getFlagsByInt(int flags) {
        EnumSet<UserFlag> set = EnumSet.noneOf(UserFlag.class);
        if (flags == 0)
            return set;
        for (UserFlag flag : UserFlag.values()) {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

    public static EnumSet<ApplicationFlag> getApplicationFlagsByInt(int flags) {
        EnumSet<ApplicationFlag> set = EnumSet.noneOf(ApplicationFlag.class);
        if (flags == 0)
            return set;
        for (ApplicationFlag flag : ApplicationFlag.values()) {
            if ((flag.getLeftShiftId() & flags) == flag.getLeftShiftId())
                set.add(flag);
        }
        return set;
    }

}
