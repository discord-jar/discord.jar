package com.seailz.javadiscordwrapper.utils.version;

/**
 * Represents the state of an API version
 * Discord.jv will use the latest version of the API unless specified otherwise
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.utils.version.APIVersion
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
