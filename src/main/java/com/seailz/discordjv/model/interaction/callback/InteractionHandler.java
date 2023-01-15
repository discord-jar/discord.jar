package com.seailz.discordjv.model.interaction.callback;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.interaction.EditInteractionMessageAction;
import com.seailz.discordjv.action.interaction.followup.InteractionFollowupAction;
import com.seailz.discordjv.model.interaction.callback.internal.InteractionHandlerImpl;
import com.seailz.discordjv.model.message.Message;

/**
 * Class for handling interactions.
 */
public interface InteractionHandler {

    InteractionFollowupAction followup(String content);

    Message getOriginalResponse();
    // TODO: editing

    void deleteOriginalResponse();

    Message getFollowup(String id);

    void deleteFollowup(String id);

    EditInteractionMessageAction editOriginalResponse();

    EditInteractionMessageAction editFollowup(String id);

    void defer(boolean ephemeral);

    default void defer() {
        defer(false);
    }

    /**
     * Only valid for component-based interactions.
     */
    void deferEdit();


    String getToken();

    String getId();

    /**
     * Creates a new InteractionHandler.
     * @param token The token of the interaction.
     * @param id The id of the interaction.
     * @param discordJv The DiscordJv instance.
     * @return The new InteractionHandler.
     */
    static InteractionHandler from(String token, String id, DiscordJv discordJv) {
        return new InteractionHandlerImpl(token, id, discordJv);
    }


}
