package com.seailz.javadiscordwrapper.utils.version;

/**
 * Represents a version of the Discord API
 *
 * @author Seailz
 * @since 1.0
 */
public enum APIVersion {

    V10(APIVersionState.AVAILABLE, 10),
    V9(APIVersionState.AVAILABLE, 9),
    V8(APIVersionState.DEPRECATED, 8),
    V7(APIVersionState.DEPRECATED, 7),
    // default version
    V6(APIVersionState.DEPRECATED, 6),
    V5(APIVersionState.DISCONTINUED, 5),
    V4(APIVersionState.DISCONTINUED, 4),
    V3(APIVersionState.DISCONTINUED, 3),
    V2(APIVersionState.DISCONTINUED, 2),
    V1(APIVersionState.DISCONTINUED, 1);

    private APIVersionState state;
    private int code;

    APIVersion(APIVersionState state, int code) {
        this.state = state;
        this.code = code;
    }

    public static APIVersion getLatest() {
        APIVersion latest = APIVersion.V1;
        for (APIVersion value : values())
            if (value.getCode() > latest.getCode())
                latest = value;
        return latest;
    }

    public APIVersionState getState() {
        return state;
    }

    public int getCode() {
        return code;
    }
}
