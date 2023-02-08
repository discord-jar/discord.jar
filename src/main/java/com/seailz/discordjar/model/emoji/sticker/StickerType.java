package com.seailz.discordjar.model.emoji.sticker;

/**
 * Represents the type of a sticker
 *
 * @author Seailz
 * @see Sticker
 * @since 1.0
 */
public enum StickerType {

    // Included in a pack available with Nitro
    STANDARD(1),
    // A sticker uploaded to a guild for the guild's members
    GUILD(2);

    private final int code;

    StickerType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StickerType getStickerTypeByCode(int code) {
        for (StickerType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
