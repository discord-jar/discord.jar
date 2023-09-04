package com.seailz.discordjar.action.interaction;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjar.model.interaction.callback.InteractionHandler;
import com.seailz.discordjar.model.interaction.reply.InteractionReply;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Used to react to an interaction.
 * This is an internal class to react to an interaction and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @since 1.0
 */
public class InteractionCallbackAction {

    private final InteractionCallbackType type;
    private final InteractionReply reply;
    private final String token;
    private final String id;
    private final DiscordJar discordJar;

    public InteractionCallbackAction(InteractionCallbackType type, InteractionReply reply, String token, String id, DiscordJar discordJar) {
        this.type = type;
        this.reply = reply;
        this.token = token;
        this.id = id;
        this.discordJar = discordJar;
    }

    public InteractionCallbackType getType() {
        return type;
    }

    public InteractionReply getReply() {
        return reply;
    }

    public Response<InteractionHandler> run() {
        JSONObject json = new JSONObject();
        json.put("type", this.type.getCode());
        json.put("data", this.reply.compile());

        DiscordRequest request =
                new DiscordRequest(json, new HashMap<>(),
                        URLS.POST.INTERACTIONS.CALLBACK.replace("{interaction.id}", this.id)
                                .replace("{interaction.token}", this.token), discordJar, URLS.POST.INTERACTIONS.CALLBACK,
                        RequestMethod.POST);
        Response<InteractionHandler> response = new Response<>();
        try {
            if (getReply().useFiles()) {
                List<File> files = getReply().getFiles();
                File[] filesArray = new File[files.size()];
                filesArray = files.toArray(filesArray);
                request.invokeWithFiles(filesArray);
            } else
                request.invoke();
            response.complete(InteractionHandler.from(token, id, discordJar));
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            response.completeError(new Response.Error(e.getCode(), e.getMessage(), e.getBody()));
        }
        return response;
    }


}
