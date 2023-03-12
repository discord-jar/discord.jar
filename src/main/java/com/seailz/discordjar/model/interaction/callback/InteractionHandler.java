package com.seailz.discordjar.model.interaction.callback;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.interaction.EditInteractionMessageAction;
import com.seailz.discordjar.action.interaction.followup.InteractionFollowupAction;
import com.seailz.discordjar.model.interaction.callback.internal.InteractionHandlerImpl;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.rest.DiscordRequest;

/**
 * Class for handling interactions.
 */
public interface InteractionHandler {

    InteractionFollowupAction followup(String content);

    Message getOriginalResponse() throws DiscordRequest.UnhandledDiscordAPIErrorException;
    // TODO: editing

    void deleteOriginalResponse() throws DiscordRequest.UnhandledDiscordAPIErrorException;

    Message getFollowup(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException;

    void deleteFollowup(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException;

    EditInteractionMessageAction editOriginalResponse() throws DiscordRequest.UnhandledDiscordAPIErrorException;

    EditInteractionMessageAction editFollowup(String id) throws DiscordRequest.UnhandledDiscordAPIErrorException;

    void defer(boolean ephemeral) throws DiscordRequest.UnhandledDiscordAPIErrorException;

    default void defer() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        defer(false);
    }

    /**
     * Only valid for component-based interactions.
     */
    void deferEdit() throws DiscordRequest.UnhandledDiscordAPIErrorException;


    String getToken();

    String getId();

    /**
     * Creates a new InteractionHandler.
     * @param token The token of the interaction.
     * @param id The id of the interaction.
     * @param discordJar The DiscordJar instance.
     * @return The new InteractionHandler.
     */
    static InteractionHandler from(String token, String id, DiscordJar discordJar) {
        return new InteractionHandlerImpl(token, id, discordJar);
    }


}
