package com.seailz.discordjv.command.listeners;

import com.seailz.discordjv.command.annotation.CommandInfo;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.model.commands.CommandType;

/**
 * This interface is used to create a command listener,
 * used to listen for commands being executed.
 * <p>
 * The interface here is just a base interface, for other command listeners like {@link SlashCommandListener} & {@link MessageContextCommandListener}.
 * It's also used to register commands.
 * <p>
 * This interface shouldn't be implemented directly, instead use a sub interface.
 * <p>
 * All implementations of this interface <b>must</b> have the {@link CommandInfo} annotation.
 * The annotation is used to define information about the command listener to give to the command dispatcher & register.
 *
 * @author Seailz
 * @see SlashCommandListener
 * @see MessageContextCommandListener
 * @see UserContextCommandListener
 * @see com.seailz.discordjv.DiscordJv#registerCommands(CommandListener...)
 * @see com.seailz.discordjv.model.interaction.data.command.ApplicationCommandInteractionData
 * @see com.seailz.discordjv.events.model.interaction.command.UserContextCommandInteractionEvent
 * @see com.seailz.discordjv.events.model.interaction.command.MessageContextCommandInteractionEvent
 * @see com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent
 * @since 1.0
 */
public interface CommandListener {

    /**
     * The type of command the sub-listener is listening for.
     *
     * @return A member of the {@link CommandType} enum.
     */
    CommandType getType();

    /**
     * The logic to be executed when the command is executed.
     *
     * @param event The event that was fired.
     */
    void onCommand(CommandInteractionEvent event);

}
