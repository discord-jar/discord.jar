package com.seailz.discordjv.events.model.interaction.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

public class MessageContextCommandInteractionEvent extends CommandInteractionEvent {

    public MessageContextCommandInteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the context menu command's target as a {@link Message}.
     *
     * @return {@link Message} object containing the message data.
     */
    @Nullable
    public Message getTarget() {
        DiscordResponse response =
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.CHANNELS.GET_MESSAGE.replace("{channel_id}", getInteraction().channel().id()).replace("{message_id}", String.valueOf(getCommandData().targetId())),
                        getBot(),
                        URLS.GET.CHANNELS.GET_MESSAGE,
                        RequestMethod.GET
                ).invoke();
        return Message.decompile(response.body(), getBot());
    }

}
