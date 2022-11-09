package com.seailz.javadiscordwrapper.utils.discordapi;

public record RateLimit(
        int limit,
        int remaining,
        int resetAfter
) {
}
