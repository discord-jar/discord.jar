package com.seailz.javadiscordwrapper.model.interaction;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.interaction.data.command.ApplicationCommandInteractionData;
import com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.javadiscordwrapper.model.interaction.data.modal.ModalSubmitInteractionData;
import org.json.JSONObject;

/**
 * Represents the data of an interaction
 * This class does not have any variables, and is just used to mark the data of an interaction.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class InteractionData {

    public static InteractionData decompile(InteractionType type, JSONObject obj, DiscordJv jv) {
        return switch (type) {
            case APPLICATION_COMMAND, APPLICATION_COMMAND_AUTOCOMPLETE -> new ApplicationCommandInteractionData(obj, jv);
            case MESSAGE_COMPONENT -> new MessageComponentInteractionData(obj);
            case MODAL_SUBMIT -> new ModalSubmitInteractionData(obj);
            default -> null;
        };
    }

}
