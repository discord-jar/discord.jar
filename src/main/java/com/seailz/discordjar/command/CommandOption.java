package com.seailz.discordjar.command;

import com.seailz.discordjar.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

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
        List<CommandOption> options,
        List<ChannelType> channelTypes,
        int minValue,
        int maxValue,
        int minLength,
        int maxLength,
        boolean autocomplete
) implements Compilerable {

    public CommandOption(String name, String description, CommandOptionType type, boolean required) {
        this(name, description, type, required, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), -1, -1, -1, -1, false);
    }

    public CommandOption addChoice(CommandChoice choice) {
        choices.add(choice);
        return this;
    }

    public CommandOption setAutocomplete(boolean autocomplete) {
        return new CommandOption(name, description, type, required, choices, subCommands, options, channelTypes, minValue, maxValue, minLength, maxLength, autocomplete);
    }

    public CommandOption setMinValue(int minValue) {
        return new CommandOption(name, description, type, required, choices, subCommands, options, channelTypes, minValue, maxValue, minLength, maxLength, autocomplete);
    }

    public CommandOption setMaxValue(int maxValue) {
        return new CommandOption(name, description, type, required, choices, subCommands, options, channelTypes, minValue, maxValue, minLength, maxLength, autocomplete);
    }

    public CommandOption setMinLength(int minLength) {
        return new CommandOption(name, description, type, required, choices, subCommands, options, channelTypes, minValue, maxValue, minLength, maxLength, autocomplete);
    }

    public CommandOption setMaxLength(int maxLength) {
        return new CommandOption(name, description, type, required, choices, subCommands, options, channelTypes, minValue, maxValue, minLength, maxLength, autocomplete);
    }

    public CommandOption addChannelType(ChannelType channelType) {
        channelTypes.add(channelType);
        return this;
    }

    public CommandOption addSubCommand(SlashSubCommand subCommand) {
        subCommands.add(subCommand);
        return this;
    }

    @Override
    public SJSONObject compile() {
        SJSONArray choicesJson = new SJSONArray();
        if (this.choices != null) {
            choices.forEach((commandChoice -> choicesJson.put(commandChoice.compile())));
        }

        SJSONArray subCommandsJson = new SJSONArray();
        if (this.subCommands != null) {
            subCommands.forEach((subCommand -> subCommandsJson.put(subCommand.compile())));
        }

        SJSONObject obj = new SJSONObject()
                .put("name", name)
                .put("description", description)
                .put("type", type.getCode());

        if (type != CommandOptionType.SUB_COMMAND && type != CommandOptionType.SUB_COMMAND_GROUP)
            obj.put("required", required);

        if (this.choices != null && !this.choices.isEmpty())
            obj.put("choices", choicesJson);
        if (this.subCommands != null && !this.subCommands.isEmpty())
            obj.put("options", subCommandsJson);
        if (this.options != null && !this.options.isEmpty()) {
            SJSONArray optionsJson = new SJSONArray();
            options.forEach((option) -> optionsJson.put(option.compile()));
            obj.put("options", optionsJson);
        }

        if (this.channelTypes != null && !this.channelTypes.isEmpty()) {
            SJSONArray channelTypesJson = new SJSONArray();
            channelTypes.forEach((channelType) -> channelTypesJson.put(channelType.getCode()));
            obj.put("channel_types", channelTypesJson);
        }

        if (this.minValue != -1 && this.minValue != 0) {
            obj.put("min_value", minValue);
        }

        if (this.maxValue != 0 && this.maxValue != -1)
            obj.put("max_value", maxValue);

        if (this.minLength != 0 && this.minLength != -1)
            obj.put("min_length", minLength);

        if (this.maxLength != 0 && this.maxLength != -1)
            obj.put("max_length", maxLength);

        obj.put("autocomplete", autocomplete);
        return obj;
    }

    public static CommandOption decompile(SJSONObject obj) {
        String name = obj.has("name") ? obj.getString("name") : null;
        String description = obj.has("description") ? obj.getString("description") : null;
        CommandOptionType type = obj.has("type") ? CommandOptionType.fromCode(obj.getInt("type")) : CommandOptionType.STRING;
        boolean required = obj.has("required") && obj.getBoolean("required");
        List<CommandChoice> choices = new ArrayList<>();
        List<CommandOption> options = new ArrayList<>();
        List<ChannelType> channelTypes = new ArrayList<>();
        int minValue = obj.has("min_value") ? obj.getInt("min_value") : 0;
        int maxValue = obj.has("max_value") ? obj.getInt("max_value") : 0;
        int minLength = obj.has("min_length") ? obj.getInt("min_length") : 0;
        int maxLength = obj.has("max_length") ? obj.getInt("max_length") : 0;
        boolean autocomplete = obj.has("autocomplete") && obj.getBoolean("autocomplete");

        if (obj.has("choices")) {
            for (Object v : obj.getJSONArray("choices")) {
                choices.add(CommandChoice.decompile((SJSONObject) v));
            }
        }

        List<SlashSubCommand> subCommands = new ArrayList<>();
        if (obj.has("options") && type == CommandOptionType.SUB_COMMAND_GROUP) {
            for (Object v : obj.getJSONArray("options")) {
                subCommands.add(SlashSubCommand.decompile((SJSONObject) v));
            }
        }

        if (obj.has("options") && type == CommandOptionType.SUB_COMMAND) {
            for (Object v : obj.getJSONArray("options")) {
                options.add(CommandOption.decompile((SJSONObject) v));
            }
        }

        if (obj.has("channel_types")) {
            for (Object v : obj.getJSONArray("channel_types")) {
                channelTypes.add(ChannelType.fromCode((int) v));
            }
        }



        return new CommandOption(
                name,
                description,
                type,
                required,
                choices,
                subCommands,
                options,
                channelTypes,
                minValue,
                maxValue,
                minLength,
                maxLength,
                autocomplete
        );
    }
}