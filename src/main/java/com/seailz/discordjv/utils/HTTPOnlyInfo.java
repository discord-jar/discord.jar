package com.seailz.discordjv.utils;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.version.APIVersion;

import java.util.EnumSet;

/**
 * POJO that contains information for discord.jv about
 * <br>initializing an HTTP-Only (Interaction-only) bot.
 * <p>
 * See {@link DiscordJv#DiscordJv(String, EnumSet, APIVersion, boolean, HTTPOnlyInfo)} for more information.
 */
public record HTTPOnlyInfo(
        String endpoint,
        String applicationPublicKey
) {
}
