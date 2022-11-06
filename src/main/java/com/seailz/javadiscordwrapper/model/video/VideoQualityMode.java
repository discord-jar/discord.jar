package com.seailz.javadiscordwrapper.model.video;

public enum VideoQualityMode {

    AUTO(1),
    FULL(2);

    private final int code;

    VideoQualityMode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static VideoQualityMode fromCode(int code) {
        for (VideoQualityMode mode : values()) {
            if (mode.getCode() == code) {
                return mode;
            }
        }
        return null;
    }

}
