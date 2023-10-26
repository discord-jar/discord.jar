package com.seailz.discordjar.model.interaction;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.interaction.data.command.ApplicationCommandInteractionData;
import com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.discordjar.model.interaction.data.modal.ModalSubmitInteractionData;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents the data of an interaction
 * This class does not have any variables, and is just used to mark the data of an interaction.
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @since 1.0
 */
public class InteractionData {

    public static InteractionData decompile(InteractionType type, JSONObject obj, DiscordJar jv) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, DiscordRequest.UnhandledDiscordAPIErrorException {
        return switch (type) {
            case APPLICATION_COMMAND, APPLICATION_COMMAND_AUTOCOMPLETE ->
                    new ApplicationCommandInteractionData(obj, jv);
            case MESSAGE_COMPONENT -> new MessageComponentInteractionData(obj, jv);
            case MODAL_SUBMIT -> new ModalSubmitInteractionData(obj);
            default -> null;
        };
    }

}
