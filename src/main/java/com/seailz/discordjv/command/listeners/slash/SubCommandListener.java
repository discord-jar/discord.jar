package com.seailz.discordjv.command.listeners.slash;

import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjv.model.commands.CommandType;

public interface SubCommandListener extends CommandListener {

    void onSubCommand(SlashCommandInteractionEvent event);

    default void onCommand(CommandInteractionEvent event) {
        onSubCommand((SlashCommandInteractionEvent) event);
    }

    @Override
    default CommandType getType() {
        return CommandType.SLASH_COMMAND;
    }

}
