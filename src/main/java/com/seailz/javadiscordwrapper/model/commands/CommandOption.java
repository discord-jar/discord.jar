package com.seailz.javadiscordwrapper.model.commands;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public record CommandOption(
        String name,
        String description,
        int type,
        boolean required,
        List<CommandChoice> choices
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONArray choicesJson = new JSONArray();
        choices.forEach((commandChoice -> choicesJson.put(commandChoice.compile())));

        return new JSONObject()
                .put("name", name)
                .put("description", description)
                .put("type", type)
                .put("required", required)
                .put("choices", choicesJson);
    }

    public static CommandOption decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String description = obj.has("description") ? obj.getString("description") : null;
        int type = obj.has("type") ? obj.getInt("type") : 0;
        boolean required = obj.has("required") && obj.getBoolean("required");
        List<CommandChoice> choices = new ArrayList<>();

        if (obj.has("choices")) {
            for (Object v : obj.getJSONArray("choices")) {
                choices.add(CommandChoice.decompile((JSONObject) v));
            }
        }

        return new CommandOption(
                name,
                description,
                type,
                required,
                choices
        );
    }
}