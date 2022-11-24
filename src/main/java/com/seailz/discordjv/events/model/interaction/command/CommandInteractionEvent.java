package com.seailz.discordjv.events.model.interaction.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.interaction.InteractionEvent;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.interaction.data.command.ApplicationCommandInteractionData;
import com.seailz.discordjv.model.interaction.data.command.ResolvedCommandOption;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

public class CommandInteractionEvent extends InteractionEvent {
    public CommandInteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the name of the command that was executed.
     *
     * @return String containing the name of the command.
     */
    @NotNull
    public String getName() {
        return getCommandData().name();
    }

    /**
     * Returns the options that were passed to the command.
     *
     * @return List of {@link ResolvedCommandOption} objects containing the options.
     */
    @NotNull
    public List<ResolvedCommandOption> getOptions() {
        return getCommandData().options();
    }

    /**
     * Returns the guild that the command was executed in.
     * This will return null if the command was not executed in a guild, and in a DM.
     *
     * @return {@link Guild} object containing the guild data.
     */
    @Nullable
    public Guild getGuild() {
        return getInteraction().guild();
    }

    @NotNull
    public ApplicationCommandInteractionData getCommandData() {
        return (ApplicationCommandInteractionData) getInteraction().data();
    }

    /**
     * Returns the context menu command's target as a {@link Message}.
     * This will return null if the targeted entity is not a message.
     *
     * @return {@link Message} object containing the message data.
     */
    @Nullable
    public Message getTargetAsMessage() {
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

    /**
     * Returns the context menu command's target as a {@link User}.
     * This will return null if the targeted entity is not a user.
     *
     * @return {@link User} object containing the user data.
     */
    @Nullable
    public User getTargetAsUser() {
        DiscordResponse response =
                new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        URLS.GET.USER.GET_USER.replace("{user_id}", String.valueOf(getCommandData().targetId())),
                        getBot(),
                        URLS.GET.USER.GET_USER,
                        RequestMethod.GET
                ).invoke();
        return User.decompile(response.body(), getBot());
    }
}