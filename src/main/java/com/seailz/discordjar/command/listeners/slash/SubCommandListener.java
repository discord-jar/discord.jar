package com.seailz.discordjar.command.listeners.slash;

import com.seailz.discordjar.command.listeners.CommandListener;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.command.CommandType;

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
