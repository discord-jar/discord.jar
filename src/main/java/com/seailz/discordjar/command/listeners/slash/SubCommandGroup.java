package com.seailz.discordjar.command.listeners.slash;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.Checker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SubCommandGroup implements Compilerable {

    private final HashMap<SlashSubCommand, SubCommandListener> subCommands = new HashMap<>();
    private String name;
    private String description;

    public SubCommandGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashMap<SlashSubCommand, SubCommandListener> getSubCommands() {
        return subCommands;
    }

    public SubCommandGroup addSubCommand(SlashSubCommand subCommand, SubCommandListener listener) {
        subCommands.put(subCommand, listener);
        return this;
    }

    @Override
    public JSONObject compile() {
        JSONArray subCommandsJson = new JSONArray();
        subCommands.keySet().forEach((subCommand -> subCommandsJson.put(subCommand.compile())));

        Checker.notNull(name, "Name cannot be null");
        Checker.notNull(description, "Description cannot be null");

        return new JSONObject()
                .put("type", 1)
                .put("description", description)
                .put("name", name)
                .put("options", subCommandsJson);
    }

}
