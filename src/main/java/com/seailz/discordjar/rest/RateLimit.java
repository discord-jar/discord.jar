package com.seailz.discordjar.rest;

public record RateLimit(
        double limit,
        double remaining,
        double resetAfter
) {
}
