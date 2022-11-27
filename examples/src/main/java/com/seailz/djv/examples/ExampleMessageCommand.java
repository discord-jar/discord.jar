package com.seailz.djv.examples;

import com.seailz.discordjv.command.annotation.CommandInfo;
import com.seailz.discordjv.command.listeners.MessageContextCommandListener;
import com.seailz.discordjv.events.model.interaction.command.MessageContextCommandInteractionEvent;

/**
 * This is an example of a message context command implementation.
 * See {@link com.seailz.djv.examples.ExampleCommand ExampleCommand} for more information.
 */
@CommandInfo(
        name = "message",
        description = "This is an example of a message context command."
)
public class ExampleMessageCommand implements MessageContextCommandListener {
    @Override
    public void onCommand(MessageContextCommandInteractionEvent mccie) {
        mccie.reply("You executed a message context command!");
    }
}
