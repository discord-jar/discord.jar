package com.seailz.discordjar.command.listeners;

import com.seailz.discordjar.command.CommandType;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.events.model.interaction.command.UserContextCommandInteractionEvent;

public interface UserContextCommandListener extends CommandListener {
    /**
     * Returns the type of command this listener is listening for.
     */
    @Override
    default CommandType getType() {
        return CommandType.USER;
    }

    /**
     * @param event The event that was fired.
     */
    void onCommand(UserContextCommandInteractionEvent event);

    /**
     * This method should not be overriden, it's used to call the {@link #onCommand(UserContextCommandInteractionEvent)} method by the command dispatcher.
     */
    @Override
    default void onCommand(CommandInteractionEvent event) {
        onCommand((UserContextCommandInteractionEvent) event);
    }
}
