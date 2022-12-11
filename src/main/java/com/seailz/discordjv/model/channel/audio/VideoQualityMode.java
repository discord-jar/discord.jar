package com.seailz.discordjv.model.channel.audio;

/**
 * The video quality mode of a {@link com.seailz.discordjv.model.channel.VoiceChannel VoiceChannel}.
 */
public enum VideoQualityMode {

    // Discord chooses the quality for optimal performance
    AUTO(1),
    // 720p
    FULL(2);

    private final int code;

    VideoQualityMode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static VideoQualityMode fromCode(int code) {
        for (VideoQualityMode mode : values()) {
            if (mode.code() == code) {
                return mode;
            }
        }
        return null;
    }

}
