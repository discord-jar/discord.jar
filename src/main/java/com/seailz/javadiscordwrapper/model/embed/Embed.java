package com.seailz.javadiscordwrapper.model.embed;

public record Embed(
        String title,
        EmbedType type,
        String description,
        String url,
        String timestamp,
        int color,
        EmbedFooter footer,
        EmbedImage image,
        EmbedImage thumbnail,
        EmbedImage video, // they use the same parameters, so this does not matter
        EmbedProvider provider,
        EmbedAuthor author,
        EmbedField[] fields
) {
}
