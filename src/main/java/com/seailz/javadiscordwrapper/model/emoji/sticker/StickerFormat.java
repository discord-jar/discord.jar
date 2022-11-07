package com.seailz.javadiscordwrapper.model.emoji.sticker;

/**
 * Represents the format that a sticker is saved in
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.emoji.sticker.Sticker
 */
public enum StickerFormat {

    PNG(1),
    APNG(2),
    LOTTIE(3);

    private int code;

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
