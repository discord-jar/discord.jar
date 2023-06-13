package com.seailz.discordjar.cache;

/**
 * Different types of objects that discord.jar will cache.
 * This can be customized in {@link com.seailz.discordjar.DiscordJarBuilder DiscordJarBuilder} to only cache certain objects.
 *
 * @author Seailz
 */
public enum CacheType {

    GUILDS,
    CHANNELS,
    MEMBERS,
    USERS,
    ALL

}
