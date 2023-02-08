package com.seailz.discordjar.model.embed;

/**
 * Represents the type of embed included in a message.
 * Embeds do not have to be "RICH", which represents a message embed created by a bot.
 */
public enum EmbedType {
    RICH,
    IMAGE,
    VIDEO,
    GIFV,
    ARTICLE,
    LINK
}
