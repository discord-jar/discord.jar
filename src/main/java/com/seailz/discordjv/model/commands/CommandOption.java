package com.seailz.discordjv.model.commands;

import com.seailz.discordjv.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjv.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an option of an app {@link Command}.
 *
 * @param name        The name of the option.
 * @param description The description of the option.
 * @param type        The type of the option.
 * @param required    Whether the option is required.
 * @param choices     Any choices the option has.
 */
public record CommandOption(
        String name,
        String description,
        CommandOptionType type,
        boolean required,
        List<CommandChoice> choices,
        List<SlashSubCommand> subCommands,
        List<CommandOption> options
) implements Compilerable {

    public CommandOption(String name, String description, CommandOptionType type, boolean required) {
        this(name, description, type, required, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public CommandOption addChoice(CommandChoice choice) {
        choices.add(choice);
        return this;
    }

    public CommandOption addSubCommand(SlashSubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public JSONObject compile() {
        JSONArray choicesJson = new JSONArray();
        if (this.choices != null) {
            choices.forEach((commandChoice -> choicesJson.put(commandChoice.compile())));
        }

        JSONArray subCommandsJson = new JSONArray();
        if (this.subCommands != null) {
            subCommands.forEach((subCommand -> subCommandsJson.put(subCommand.compile())));
        }

        return new JSONObject()
                .put("name", name)
                .put("description", description)
                .put("type", type.getCode())
                .put("required", required)
                .put("choices", this.choices != null ? choicesJson : null)
                .put("options", this.subCommands != null ? subCommandsJson : null);
    }

    public static CommandOption decompile(JSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String description = obj.has("description") ? obj.getString("description") : null;
        CommandOptionType type = obj.has("type") ? CommandOptionType.fromCode(obj.getInt("type")) : CommandOptionType.STRING;
        boolean required = obj.has("required") && obj.getBoolean("required");
        List<CommandChoice> choices = new ArrayList<>();
        List<CommandOption> options = new ArrayList<>();

        if (obj.has("choices")) {
            for (Object v : obj.getJSONArray("choices")) {
                choices.add(CommandChoice.decompile((JSONObject) v));
            }
        }

        List<SlashSubCommand> subCommands = new ArrayList<>();
        if (obj.has("options") && type == CommandOptionType.SUB_COMMAND_GROUP) {
            for (Object v : obj.getJSONArray("options")) {
                subCommands.add(SlashSubCommand.decompile((JSONObject) v));
            }
        }

        if (obj.has("options") && type == CommandOptionType.SUB_COMMAND) {
            for (Object v : obj.getJSONArray("options")) {
                options.add(CommandOption.decompile((JSONObject) v));
            }
        }



        return new CommandOption(
                name,
                description,
                type,
                required,
                choices,
                subCommands,
                options
        );
    }
}