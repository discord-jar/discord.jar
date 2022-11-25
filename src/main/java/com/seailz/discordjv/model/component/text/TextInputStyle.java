package com.seailz.discordjv.model.component.text;

/**
 * Represents the style of a {@link TextInput}
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.component.text.TextInput
 * @since 1.0
 */
public enum TextInputStyle {

    /**
     * A single line text input
     */
    SHORT(1),
    /**
     * A multiline text input
     */
    LONG(2),
    /* Unknown */
    UNKNOWN(-1),

    ;
    private final int code;

    TextInputStyle(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static TextInputStyle fromCode(int code) {
        for (TextInputStyle style : TextInputStyle.values()) {
            if (style.code() == code) return style;
        }
        return UNKNOWN;
    }

}
