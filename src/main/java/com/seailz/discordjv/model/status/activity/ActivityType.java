package com.seailz.discordjv.model.status.activity;

/**
 * Represents the type of activity
 *
 * @author Seailz
 * @see Activity
 * @since 1.0
 */
public enum ActivityType {

    PLAYING(0),
    STREAMING(1),
    LISTENING(2),
    WATCHING(3),
    CUSTOM(4),
    COMPETING(5);

    private final int code;

    ActivityType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ActivityType fromCode(int code) {
        switch (code) {
            case 0 -> {
                return PLAYING;
            }
            case 1 -> {
                return STREAMING;
            }
            case 2 -> {
                return LISTENING;
            }
            case 3 -> {
                return WATCHING;
            }
            case 4 -> {
                return CUSTOM;
            }
            case 5 -> {
                return COMPETING;
            }
            default -> {
                return null;
            }
        }
    }

}
