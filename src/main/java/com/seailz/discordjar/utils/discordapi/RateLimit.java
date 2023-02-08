package com.seailz.discordjar.utils.discordapi;

public record RateLimit(
        double limit,
        double remaining,
        double resetAfter
) {
}
