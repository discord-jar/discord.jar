package com.seailz.discordjv.command.listeners.slash;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.command.CommandOption;
import com.seailz.discordjv.command.CommandOptionType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlashSubCommand implements Compilerable {

    String name;
    String description;
    List<CommandOption> options;

    public SlashSubCommand(String name, String description, List<CommandOption> options) {
        this.name = name;
        this.description = description;
        this.options = options;
    }

    public SlashSubCommand(String name, String description) {
        this(name, description, new ArrayList<>());
    }

    public List<CommandOption> getOptions() {
        return options;
    }

    public SlashSubCommand addOption(CommandOption option) {
        options.add(option);
        return this;
    }

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        json.put("type", CommandOptionType.SUB_COMMAND.getCode());
        json.put("name", name);
        json.put("description", description);
        JSONArray optionsJson = new JSONArray();
        options.forEach((option -> optionsJson.put(option.compile())));
        json.put("options", optionsJson);

        return json;
    }

    public static SlashSubCommand decompile(JSONObject obj) {
        String name = obj.getString("name");
        String description = obj.getString("description");

        List<CommandOption> options = new ArrayList<>();
        JSONArray optionsJson = obj.getJSONArray("options");
        for (int i = 0; i < optionsJson.length(); i++) {
            options.add(CommandOption.decompile(optionsJson.getJSONObject(i)));
        }

        return new SlashSubCommand(name, description, options);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
