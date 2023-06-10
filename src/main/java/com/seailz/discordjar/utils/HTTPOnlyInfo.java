package com.seailz.discordjar.utils;

import com.seailz.discordjar.DiscordJar;

/**
 * POJO that contains information for discord.jar about
 * <br>initializing an HTTP-Only (Interaction-only) bot.
 * <p>
 * See {@link DiscordJar#DiscordJar(String, boolean, HTTPOnlyInfo)} for more information.
 */
public record HTTPOnlyInfo(
        String endpoint,
        String applicationPublicKey
) {
}
