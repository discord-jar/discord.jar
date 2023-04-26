package com.seailz.discordjar.voice.ws;

/**
 * Represents the version of the voice gateway connection.
 * @author Seailz
 */
public enum VoiceGatewayVersion {

    V_4(4),
    V_3(3),
    V_2(2),
    V_1(1),

    UNKNOWN(-1);

    private final int version;

    VoiceGatewayVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public static VoiceGatewayVersion fromVersion(int version) {
        for (VoiceGatewayVersion v : VoiceGatewayVersion.values()) {
            if (v.getVersion() == version) {
                return v;
            }
        }
        return UNKNOWN;
    }

}
