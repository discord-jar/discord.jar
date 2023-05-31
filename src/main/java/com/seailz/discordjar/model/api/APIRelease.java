package com.seailz.discordjar.model.api;

/**
 * Represents an API release, for example, stable, canary, or ptb.
 * @since 1.0
 * @author Seailz
 */
public enum APIRelease {

    STABLE(""),
    CANARY("canary."),
    PTB("ptb.")
    ;

    String baseUrlPrefix;

    APIRelease(String baseUrlPrefix) {
        this.baseUrlPrefix = baseUrlPrefix;
    }

    public String getBaseUrlPrefix() {
        return baseUrlPrefix;
    }

}
