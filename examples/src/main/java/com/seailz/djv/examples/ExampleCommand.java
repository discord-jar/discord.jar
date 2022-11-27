package com.seailz.djv.examples;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.command.listeners.CommandListener;
import com.seailz.discordjv.command.listeners.SlashCommandListener;
import com.seailz.discordjv.events.model.interaction.command.SlashCommandInteractionEvent;

/**
 * This is an example of a slash command.
 * It will reply with "Hello World!" when the command is executed.
 * <p>
 * To register commands, you must use the {@link DiscordJv#registerCommands(CommandListener...)} method.
 * You can also implement other types of commands,
 * like {@link com.seailz.discordjv.command.listeners.MessageContextCommandListener MessageContextCommandListener} and {@link com.seailz.discordjv.command.listeners.UserContextCommandListener UserContextCommandListener}.
 * <p>
 * When setting up your command, you must use the {@link com.seailz.discordjv.command.annotation.CommandInfo CommandInfo} annotation to provide info about the command to discord.jv
 *
 * @author Seailz
 * @see DiscordJv#registerCommands(CommandListener...)
 * @see com.seailz.discordjv.command.listeners.MessageContextCommandListener
 * @see com.seailz.discordjv.command.listeners.UserContextCommandListener
 */
public class ExampleCommand implements SlashCommandListener {
    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.reply("Hello World!");
    }
}
