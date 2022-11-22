package com.seailz.javadiscordwrapper.model.commands;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;

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