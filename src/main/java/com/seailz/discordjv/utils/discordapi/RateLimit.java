package com.seailz.discordjv.utils.discordapi;

public record RateLimit(
        int limit,
        int remaining,
        int resetAfter
) {
}
