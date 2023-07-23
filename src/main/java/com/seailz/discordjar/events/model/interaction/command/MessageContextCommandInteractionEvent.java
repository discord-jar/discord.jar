package com.seailz.discordjar.events.model.interaction.command;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.json.SJSONObject;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

public class MessageContextCommandInteractionEvent extends CommandInteractionEvent {

    public MessageContextCommandInteractionEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
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
                null;
        try {
            response = new DiscordRequest(
                    new SJSONObject(),
                    new HashMap<>(),
                    URLS.GET.CHANNELS.GET_MESSAGE.replace("{channel.id}", getInteraction().channel().id()).replace("{message.id}", String.valueOf(getCommandData().targetId())),
                    getBot(),
                    URLS.GET.CHANNELS.GET_MESSAGE,
                    RequestMethod.GET
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
        return Message.decompile(response.body(), getBot());
    }

}
