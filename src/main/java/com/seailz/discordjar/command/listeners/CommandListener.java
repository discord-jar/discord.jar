package com.seailz.discordjar.command.listeners;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.command.CommandType;

/**
 * This interface is used to create a command listener,
 * used to listen for commands being executed.
 * <p>
 * The interface here is just a base interface, for other command listeners like {@link SlashCommandListener} & {@link MessageContextCommandListener}.
 * It's also used to register commands.
 * <p>
 * This interface shouldn't be implemented directly, instead use a sub interface.
 * <p>
 * All implementations of this interface <b>must</b> have the {@link SlashCommandInfo} annotation.
 * The annotation is used to define information about the command listener to give to the command dispatcher & register.
 *
 * @author Seailz
 * @see SlashCommandListener
 * @see MessageContextCommandListener
 * @see UserContextCommandListener
 * @see DiscordJar#registerCommands(CommandListener...)
 * @see com.seailz.discordjar.model.interaction.data.command.ApplicationCommandInteractionData
 * @see com.seailz.discordjar.events.model.interaction.command.UserContextCommandInteractionEvent
 * @see com.seailz.discordjar.events.model.interaction.command.MessageContextCommandInteractionEvent
 * @see com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent
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
