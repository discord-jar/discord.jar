package com.seailz.discordjv.model.guild;

import com.seailz.discordjv.utils.flag.Bitwiseable;

/**
 * Flags for a {@link Member} of a guild.
 * Determines some features of the member that they have.
 *
 * @author Seailz
 * @since 1.0
 */
public enum MemberFlags implements Bitwiseable {

    DID_REJOIN(0),
    COMPLETED_ONBOARDING(1),
    BYPASSES_VERIFICATION(2),
    STARTED_ONBOARDING(3);

    private final int id;

    MemberFlags(int id) {
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
