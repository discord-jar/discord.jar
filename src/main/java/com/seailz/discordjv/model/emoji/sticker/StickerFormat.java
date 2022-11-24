package com.seailz.discordjv.model.emoji.sticker;

/**
 * Represents the format that a sticker is saved in
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.emoji.sticker.Sticker
 * @since 1.0
 */
public enum StickerFormat {

    PNG(1),
    APNG(2),
    LOTTIE(3);

    private final int code;

    StickerFormat(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StickerFormat getStickerFormatByCode(int code) {
        for (StickerFormat type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

}
