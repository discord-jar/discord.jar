package examples;

import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.MessageContextCommandListener;
import com.seailz.discordjar.events.model.interaction.command.MessageContextCommandInteractionEvent;

/**
 * This is an example of a message context command implementation.
 * See {@link ExampleCommand ExampleCommand} for more information.
 */
@SlashCommandInfo(
        name = "message",
        description = "This is an example of a message context command."
)
public class ExampleMessageCommand implements MessageContextCommandListener {
    @Override
    public void onCommand(MessageContextCommandInteractionEvent mccie) {
        mccie.reply("You executed a message context command!");
    }
}