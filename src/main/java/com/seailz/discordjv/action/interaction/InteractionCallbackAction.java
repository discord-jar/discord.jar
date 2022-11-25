package com.seailz.discordjv.action.interaction;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.model.interaction.reply.InteractionReply;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Used to react to an interaction.
 * This is an internal class to react to an interaction and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.interaction.Interaction
 * @since 1.0
 */
public class InteractionCallbackAction {

    private final InteractionCallbackType type;
    private final InteractionReply reply;
    private final String token;
    private final String id;
    private final DiscordJv discordJv;

    public InteractionCallbackAction(InteractionCallbackType type, InteractionReply reply, String token, String id, DiscordJv discordJv) {
        this.type = type;
        this.reply = reply;
        this.token = token;
        this.id = id;
        this.discordJv = discordJv;
    }

    public InteractionCallbackType getType() {
        return type;
    }

    public InteractionReply getReply() {
        return reply;
    }

    public void run() {
        JSONObject json = new JSONObject();
        json.put("type", this.type.getCode());
        json.put("data", this.reply.compile());

        DiscordRequest request =
                new DiscordRequest(json, new HashMap<>(),
                        URLS.POST.INTERACTIONS.CALLBACK.replace("{interaction.id}", this.id)
                                .replace("{interaction.token}", this.token), discordJv, URLS.POST.INTERACTIONS.CALLBACK,
                        RequestMethod.POST);
        request.invoke();
    }


}
