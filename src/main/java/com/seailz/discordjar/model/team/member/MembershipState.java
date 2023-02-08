package com.seailz.discordjar.model.team.member;

public enum MembershipState {

    INVITED(1),
    ACCEPTED(2);

    private final int code;

    MembershipState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MembershipState fromCode(int code) {
        for (MembershipState state : values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        return null;
    }

}
