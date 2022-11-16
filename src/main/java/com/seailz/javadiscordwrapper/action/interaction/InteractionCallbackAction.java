package com.seailz.javadiscordwrapper.action.interaction;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.interaction.callback.InteractionCallbackType;
import com.seailz.javadiscordwrapper.model.interaction.reply.InteractionMessageResponse;
import com.seailz.javadiscordwrapper.model.interaction.reply.InteractionReply;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Used to react to an interaction.
 * This is an internal class to react to an interaction and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class InteractionCallbackAction {

    private InteractionCallbackType type;
    private InteractionReply reply;
    private String token;
    private String id;
    private DiscordJv discordJv;

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
        System.out.println(json );
        request.invoke();
    }


}
