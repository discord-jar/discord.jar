package com.seailz.discordjv.utils.discordapi;

public record RateLimit(
        double limit,
        double remaining,
        double resetAfter
) {
}
