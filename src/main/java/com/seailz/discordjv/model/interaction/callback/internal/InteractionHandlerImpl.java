package com.seailz.discordjv.model.interaction.callback.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.interaction.followup.InteractionFollowupAction;
import com.seailz.discordjv.model.interaction.callback.InteractionHandler;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

public class InteractionHandlerImpl implements InteractionHandler {

    private final String token;
    private final String id;
    private final DiscordJv discordJv;

    public InteractionHandlerImpl(String token, String id, DiscordJv discordJv) {
        this.token = token;
        this.id = id;
        this.discordJv = discordJv;
    }

    @Override
    public InteractionFollowupAction followup(String content) {
        return new InteractionFollowupAction(content, token, id, discordJv);
    }

    @Override
    public Message getOriginalResponse() {
        return Message.decompile(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.INTERACTIONS.GET_ORIGINAL_INTERACTION_RESPONSE.replace("{interaction.id}", id).replace("{application.id}", discordJv.getSelfInfo().id()),
                        discordJv,
                        URLS.GET.INTERACTIONS.GET_ORIGINAL_INTERACTION_RESPONSE,
                        RequestMethod.GET
                ).invoke().body(), discordJv
        );
    }

    @Override
    public void deleteOriginalResponse() {
        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.DELETE.INTERACTION.DELETE_ORIGINAL_INTERACTION_RESPONSE.replace("{interaction.token}", token).replace("{application.id}", discordJv.getSelfInfo().id()),
                discordJv,
                URLS.DELETE.INTERACTION.DELETE_ORIGINAL_INTERACTION_RESPONSE,
                RequestMethod.DELETE
        ).invoke();
    }

    @Override
    public Message getFollowup(String id) {
        return Message.decompile(
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.INTERACTIONS.GET_FOLLOWUP_MESSAGE.replace("{interaction.id}", id).replace("{application.id}", discordJv.getSelfInfo().id())
                                .replace("{message.id}", id),
                        discordJv,
                        URLS.GET.INTERACTIONS.GET_FOLLOWUP_MESSAGE,
                        RequestMethod.GET
                ).invoke().body(), discordJv
        );
    }

    @Override
    public void deleteFollowup(String id) {
        new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.DELETE.INTERACTION.DELETE_FOLLOWUP_MESSAGE.replace("{interaction.id}", id).replace("{application.id}", discordJv.getSelfInfo().id())
                        .replace("{message.id}", id),
                discordJv,
                URLS.DELETE.INTERACTION.DELETE_FOLLOWUP_MESSAGE,
                RequestMethod.DELETE
        ).invoke();
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
