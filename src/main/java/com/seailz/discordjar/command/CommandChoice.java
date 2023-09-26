package com.seailz.discordjar.command;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;

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
    public static CommandChoice decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String value = obj.has("value") ? obj.getString("value") : null;

        return new CommandChoice(
                name,
                value
        );
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("name", name)
                .put("value", value);
    }
}