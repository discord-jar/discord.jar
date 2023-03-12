package com.seailz.discordjar.model.channel.interfaces;

import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Used for channels that have the ability to have typing indicators.
 */
public interface Typeable extends Channel {

    /**
     * Triggers a typing indicator for the channel.
     */
    default void typing() throws DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordRequest req = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                URLS.POST.CHANNELS.TRIGGER_TYPING_INDICATOR.replace("{channel.id}", id()),
                djv(),
                URLS.POST.CHANNELS.TRIGGER_TYPING_INDICATOR,
                RequestMethod.POST
        );
        req.invoke();
    }

}
