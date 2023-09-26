package com.seailz.discordjar.model.user;

public enum PremiumType {

    // No subscription
    NONE(0, false, false),
    // Discontinued Nitro Classic
    NITRO_CLASSIC(1, false, true),
    // Nitro
    NITRO(2, true, true),
    // Nitro Basic
    NITRO_BASIC(3, false, true),

    ;

    private final int id;
    private final boolean hasBoosts;
    private final boolean isNitro;

    PremiumType(int id, boolean hasBoosts, boolean isNitro) {
        this.id = id;
        this.hasBoosts = hasBoosts;
        this.isNitro = isNitro;
    }

    public static PremiumType fromId(int id) {
        for (PremiumType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public boolean hasBoosts() {
        return hasBoosts;
    }

    public boolean isNitro() {
        return isNitro;
    }

}
