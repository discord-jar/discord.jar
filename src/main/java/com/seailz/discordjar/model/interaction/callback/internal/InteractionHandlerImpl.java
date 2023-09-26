package com.seailz.discordjar.model.interaction.callback.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.interaction.EditInteractionMessageAction;
import com.seailz.discordjar.action.interaction.followup.InteractionFollowupAction;
import com.seailz.discordjar.model.interaction.callback.InteractionHandler;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

public class InteractionHandlerImpl implements InteractionHandler {

    private final String token;
    private final String id;
    private final DiscordJar discordJar;

    public InteractionHandlerImpl(String token, String id, DiscordJar discordJar) {
        this.token = token;
        this.id = id;
        this.discordJar = discordJar;
    }

    @Override
    public InteractionFollowupAction followup(String content) {
        return new InteractionFollowupAction(content, token, id, discordJar);
    }

    @Override
    public Message getOriginalResponse() {
        try {
            return Message.decompile(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.INTERACTIONS.GET_ORIGINAL_INTERACTION_RESPONSE.replace("{interaction.token}", token).replace("{application.id}", discordJar.getSelfInfo().id()),
                            discordJar,
                            URLS.GET.INTERACTIONS.GET_ORIGINAL_INTERACTION_RESPONSE,
                            RequestMethod.GET
                    ).invoke().body(), discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public void deleteOriginalResponse() {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.INTERACTION.DELETE_ORIGINAL_INTERACTION_RESPONSE.replace("{interaction.token}", token).replace("{application.id}", discordJar.getSelfInfo().id()),
                    discordJar,
                    URLS.DELETE.INTERACTION.DELETE_ORIGINAL_INTERACTION_RESPONSE,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public Message getFollowup(String id) {
        try {
            return Message.decompile(
                    new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            URLS.GET.INTERACTIONS.GET_FOLLOWUP_MESSAGE.replace("{interaction.token}", token).replace("{application.id}", discordJar.getSelfInfo().id())
                                    .replace("{message.id}", id),
                            discordJar,
                            URLS.GET.INTERACTIONS.GET_FOLLOWUP_MESSAGE,
                            RequestMethod.GET
                    ).invoke().body(), discordJar
            );
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public void deleteFollowup(String id) {
        try {
            new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.INTERACTION.DELETE_FOLLOWUP_MESSAGE.replace("{interaction.token}", token).replace("{application.id}", discordJar.getSelfInfo().id())
                            .replace("{message.id}", id),
                    discordJar,
                    URLS.DELETE.INTERACTION.DELETE_FOLLOWUP_MESSAGE,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public EditInteractionMessageAction editOriginalResponse() {
        return new EditInteractionMessageAction(
                discordJar.getSelfInfo().id(),
                token,
                discordJar,
                true,
                null
        );
    }

    @Override
    public EditInteractionMessageAction editFollowup(String id) {
        return new EditInteractionMessageAction(
                discordJar.getSelfInfo().id(),
                token,
                discordJar,
                false,
                id
        );
    }

    @Override
    public void defer(boolean ephemeral) {
        try {
            new DiscordRequest(
                    new JSONObject().put("type", 5).put("data", new JSONObject().put("flags", ephemeral ? 64 : 0)),
                    new HashMap<>(),
                    URLS.POST.INTERACTIONS.CALLBACK.replace("{interaction.id}", id).replace("{interaction.token}", token),
                    discordJar,
                    URLS.POST.INTERACTIONS.CALLBACK,
                    RequestMethod.POST
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public void deferEdit() {
        try {
            new DiscordRequest(
                    new JSONObject().put("type", 6),
                    new HashMap<>(),
                    URLS.POST.INTERACTIONS.CALLBACK.replace("{interaction.id}", id).replace("{interaction.token}", token),
                    discordJar,
                    URLS.POST.INTERACTIONS.CALLBACK,
                    RequestMethod.POST
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getId() {
        return id;
    }
}
