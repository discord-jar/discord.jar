package com.seailz.discordjv.model.component.button;

import java.awt.*;

public enum ButtonStyle {

    // blurple
    PRIMARY(1, Color.MAGENTA),
    // grey
    SECONDARY(2, Color.GRAY),
    // green
    SUCCESS(3, Color.GREEN),
    // red
    DANGER(4, Color.RED),
    // grey (navigates to a URL)
    LINK(5, Color.GRAY),
    // unknown
    UNKNOWN(-1, null);

    private final int code;
    private final Color color;

    ButtonStyle(int code, Color color) {
        this.code = code;
        this.color = color;
    }

    public int code() {
        return code;
    }

    public static ButtonStyle fromCode(int code) {
        for (ButtonStyle style : values()) {
            if (style.code() == code) {
                return style;
            }
        }
        return UNKNOWN;
    }

}
