package com.seailz.discordjar.command;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;

/**
 * Represents a choice of a {@link CommandOption}.
 *
 * @param name  The name of the option.
 * @param value The internal value of the option.
 */
public record CommandChoice(
        String name,
        String value
) implements Compilerable {
    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("name", name)
                .put("value", value);
    }

    public static CommandChoice decompile(SJSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String value = obj.has("value") ? obj.getString("value") : null;

        return new CommandChoice(
                name,
                value
        );
    }
}