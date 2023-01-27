package com.seailz.discordjv.command.listeners.slash;

import com.seailz.discordjv.command.CommandOption;
import com.seailz.discordjv.command.CommandOptionType;
import com.seailz.discordjv.command.CommandType;
import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SlashCommandListener implements CommandListener {

    private final List<CommandOption> options = new ArrayList<>();
    private final HashMap<SlashSubCommand, SubCommandListener> subCommands = new HashMap<>();

    public void addSubCommandGroup(SubCommandGroup group) {
        options.add(new CommandOption(
                group.getName(),
                group.getDescription(),
                CommandOptionType.SUB_COMMAND_GROUP,
                true,
                new ArrayList<>(),
                group.getSubCommands().keySet().stream().toList(),
                new ArrayList<>()
        ));
        subCommands.putAll(group.getSubCommands());
    }

    public void addSubCommand(SlashSubCommand subCommand, SubCommandListener listener) {
        options.add(new CommandOption(
                subCommand.getName(),
                subCommand.getDescription(),
                CommandOptionType.SUB_COMMAND,
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                subCommand.getOptions()
        ));
        subCommands.put(subCommand, listener);
    }

    /**
     * Returns the type of command this listener is listening for.
     */
    @Override
    public CommandType getType() {
        return CommandType.SLASH_COMMAND;
    }

    /**
     * @param event The event that was fired.
     */
    protected abstract void onCommand(SlashCommandInteractionEvent event);

    /**
     * This method should not be overriden, it's used to call the {@link #onCommand(SlashCommandInteractionEvent)} method by the command dispatcher.
     */
    @Override
    public void onCommand(CommandInteractionEvent event) {
        onCommand((SlashCommandInteractionEvent) event);
    }

    public List<CommandOption> getOptions() {
        return options;
    }

    public SlashCommandListener addOption(CommandOption option) {
        options.add(option);
        return this;
    }

    public HashMap<SlashSubCommand, SubCommandListener> getSubCommands() {
        return subCommands;
    }
}
