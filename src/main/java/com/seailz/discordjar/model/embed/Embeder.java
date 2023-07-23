package com.seailz.discordjar.model.embed;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.awt.*;

public interface Embeder {

    Embeder title(String title);

    Embeder description(String description);

    Embeder url(String url);

    Embeder timestamp(String timestamp);
    Embeder timestamp();

    Embeder removeField(String name);

    Embeder field(EmbedField field);
    Embeder field(EmbedField field, int index);
    Embeder field(String name, String value);
    Embeder field(String name, String value, int index);

    Embeder field(String name, String value, boolean inline);
    Embeder field(String name, String value, boolean inline, int index);

    Embeder color(Color color);
    Embeder color(int color);

    Embeder footer(EmbedFooter footer);

    Embeder footer(String text, String iconUrl);

    Embeder image(EmbedImage image);

    Embeder image(String url);

    Embeder thumbnail(EmbedImage thumbnail);

    Embeder thumbnail(String url);

    Embeder author(EmbedAuthor author);

    Embeder author(String name, String url, String iconUrl);

    SJSONObject compile();

    @Contract(value = " -> new", pure = true)
    static @NotNull Embeder e() {
        return new EmbederImpl();
    }


}
