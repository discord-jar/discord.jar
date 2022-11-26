package com.seailz.discordjv.model.commands;

import com.seailz.discordjv.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an application command.
 * @param name The name of the app command
 * @param type The type of the app command. See {@link CommandType}
 * @param description The description of the app command.
 * @param options Any options the app command will have.
 * @author itstotallyjan
 */
public record Command(
        String name,
        CommandType type,
        String description,
        List<CommandOption> options
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONArray optionsJson = new JSONArray();
        options.forEach((option) -> optionsJson.put(option.compile()));

        return new JSONObject()
                .put("name", name)
                .put("type", type.getCode())
                .put("description", description)
                .put("options", optionsJson);
    }

    public static Command decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        CommandType type = obj.has("type") ? CommandType.fromCode(obj.getInt("type")) : CommandType.UNKNOWN;
        String description = obj.has("description") ? obj.getString("description") : null;
        List<CommandOption> options = new ArrayList<>();

        if (obj.has("options")) {
            for (Object v : obj.getJSONArray("options")) {
                options.add(CommandOption.decompile((JSONObject) v));
            }
        }

        return new Command(
                name,
                type,
                description,
                options
        );
    }
}
