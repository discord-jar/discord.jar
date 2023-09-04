package com.seailz.discordjar.model.api.version;

import com.seailz.discordjar.model.api.version.APIVersion;

/**
 * Represents the state of an API version
 * discord.jar will use the latest version of the API unless specified otherwise
 *
 * @author Seailz
 * @see APIVersion
 * @since 1.0
 */
public enum APIVersionState {

    AVAILABLE(true),
    DEPRECATED(true),
    DISCONTINUED(false),

    ;
    boolean canBeUsed;

    APIVersionState(boolean canBeUsed) {
        this.canBeUsed = canBeUsed;
    }


}
