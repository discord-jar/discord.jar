package com.seailz.discordjar.utils;

/**
 * Represents a mentionable entity. A mentionable entity
 * is an entity that can be mentioned in a message.
 *
 * <p>These mentions (behind the scenes) look like this:
 * <pre>
 *     {@code <{PREFIX}{ID}>}
 * </pre>
 *
 * For example, my user mention would look like this:
 * <pre>
 *     {@code <@947691195658797167>}
 * </pre>
 * since the prefix for a user mention is {@code @} and my
 * user id is {@code 947691195658797167}.
 *
 * <br>These are displayed formatted by the Discord client.
 * <br>At the moment, the only mentionable entities are {@link com.seailz.discordjar.model.user.User users}, {@link com.seailz.discordjar.model.channel.Channel channels}, and {@link com.seailz.discordjar.model.role.Role roles}.
 * <p>
 * Note: If this has changed when you are reading this, please open an issue or pull request <a href="https://www.github.com/discord.jar/discord.jar">here</a>. Thanks :)
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.role.Role
 * @see com.seailz.discordjar.model.user.User
 * @see com.seailz.discordjar.model.channel.Channel
 * @since 1.0
 */
public interface Mentionable {

    /**
     * Returns the ID of the current mentionable entity.
     * @return {@link String}
     */
    String id();

    /**
     * The prefix that comes after the first {@code <} but before the id in a mention.
     * @return {@link String}
     */
    String getMentionablePrefix();

    /**
     * Returns the current mentionable entity as a mention.
     * @return {@link String}
     */
    default String getAsMention() {
        return "<" + getMentionablePrefix() + id() + ">";
    }

}
