package com.seailz.discordjv.model.commands;

import com.seailz.discordjv.core.Compilerable;
import org.json.JSONObject;

/**
 * Represents a choice of a {@link CommandOption}.
 * @param name The name of the option.
 * @param value The internal value of the option.
 */
public record CommandChoice(
        String name,
        String value
) implements Compilerable {
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("name", name)
                .put("value", value);
    }

    public static CommandChoice decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String value = obj.has("value") ? obj.getString("value") : null;

        return new CommandChoice(
                name,
                value
        );
    }
}