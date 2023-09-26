package com.seailz.discordjar.command.listeners;

import com.seailz.discordjar.command.CommandType;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.MessageContextCommandInteractionEvent;

public interface MessageContextCommandListener extends CommandListener {
    /**
     * Returns the type of command this listener is listening for.
     */
    @Override
    default CommandType getType() {
        return CommandType.MESSAGE;
    }

    /**
     * @param event The event that was fired.
     */
    void onCommand(MessageContextCommandInteractionEvent event);

    /**
     * This method should not be overriden, it's used to call the {@link #onCommand(MessageContextCommandInteractionEvent)} method by the command dispatcher.
     */
    @Override
    default void onCommand(CommandInteractionEvent event) {
        onCommand((MessageContextCommandInteractionEvent) event);
    }
}
