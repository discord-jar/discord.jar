package com.seailz.javadiscordwrapper.model.commands;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public record Command(
        String name,
        int type,
        String description,
        List<CommandOption> options
) implements Compilerable {
    @Override
    public JSONObject compile() {
        JSONArray optionsJson = new JSONArray();
        options.forEach((option) -> optionsJson.put(option.compile()));

        return new JSONObject()
                .put("name", name)
                .put("type", type)
                .put("description", description)
                .put("options", optionsJson);
    }

    public static Command decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        int type = obj.has("type") ? obj.getInt("type") : null;
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
