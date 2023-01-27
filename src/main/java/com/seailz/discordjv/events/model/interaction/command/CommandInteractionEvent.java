package com.seailz.discordjv.events.model.interaction.command;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.interaction.ModalInteractionCallbackAction;
import com.seailz.discordjv.events.model.interaction.InteractionEvent;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.model.interaction.data.command.ApplicationCommandInteractionData;
import com.seailz.discordjv.model.interaction.modal.Modal;
import com.seailz.discordjv.model.interaction.reply.InteractionModalResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

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

    public ModalInteractionCallbackAction replyModal(Modal modal) {
        return new ModalInteractionCallbackAction(
                InteractionCallbackType.MODAL,
                new InteractionModalResponse(modal.title(), modal.customId(), modal.components()),
                getInteraction().token(),
                getInteraction().id(),
                getBot()
        );
    }
}