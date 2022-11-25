package com.seailz.javadiscordwrapper.model.commands;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an option of an app {@link Command}.
 * @param name The name of the option.
 * @param description The description of the option.
 * @param type The type of the option.
 * @param required Whether the option is required.
 * @param choices Any choices the option has.
 */
public record CommandOption(
        String name,
        String description,
        CommandOptionType type,
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
                .put("type", type.getCode())
                .put("required", required)
                .put("choices", choicesJson);
    }

    public static CommandOption decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String description = obj.has("description") ? obj.getString("description") : null;
        CommandOptionType type = obj.has("type") ? CommandOptionType.fromCode(obj.getInt("type")) : CommandOptionType.STRING;
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