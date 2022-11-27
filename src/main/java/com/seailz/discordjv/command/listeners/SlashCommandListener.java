package com.seailz.discordjv.command.listeners;

import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjv.model.commands.CommandOption;
import com.seailz.discordjv.model.commands.CommandType;

import java.util.ArrayList;
import java.util.List;

public interface SlashCommandListener extends CommandListener {

    List<CommandOption> options = new ArrayList<>();

    /**
     * Returns the type of command this listener is listening for.
     */
    @Override
    default CommandType getType() {
        return CommandType.SLASH_COMMAND;
    }

    /**
     * @param event The event that was fired.
     */
    void onCommand(SlashCommandInteractionEvent event);

    /**
     * This method should not be overriden, it's used to call the {@link #onCommand(SlashCommandInteractionEvent)} method by the command dispatcher.
     */
    @Override
    default void onCommand(CommandInteractionEvent event) {
        onCommand((SlashCommandInteractionEvent) event);
    }

    default List<CommandOption> getOptions() {
        return options;
    }

    default SlashCommandListener addOption(CommandOption option) {
        options.add(option);
        return this;
    }
}
