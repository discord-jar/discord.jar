package com.seailz.discordjv.model.embed;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.awt.*;

public interface Embeder {

    Embeder title(String title);

    Embeder description(String description);

    Embeder url(String url);

    Embeder timestamp(String timestamp);

    Embeder field(EmbedField field);

    Embeder field(String name, String value, boolean inline);

    Embeder color(float color);

    Embeder color(Color color);

    Embeder footer(EmbedFooter footer);

    Embeder footer(String text, String iconUrl);

    Embeder image(EmbedImage image);

    Embeder image(String url);

    Embeder thumbnail(EmbedImage thumbnail);

    Embeder thumbnail(String url);

    Embeder author(EmbedAuthor author);

    Embeder author(String name, String url, String iconUrl);

    JSONObject compile();

    @Contract(value = " -> new", pure = true)
    static @NotNull Embeder e() {
        return new EmbederImpl();
    }


}
