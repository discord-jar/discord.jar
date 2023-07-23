package com.seailz.discordjar.events.model.interaction.command;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.interaction.ModalInteractionCallbackAction;
import com.seailz.discordjar.events.model.interaction.InteractionEvent;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjar.model.interaction.data.command.ApplicationCommandInteractionData;
import com.seailz.discordjar.model.interaction.modal.Modal;
import com.seailz.discordjar.model.interaction.reply.InteractionModalResponse;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandInteractionEvent extends InteractionEvent {
    public CommandInteractionEvent(@NotNull DiscordJar bot, long sequence, @NotNull SJSONObject data) {
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

    public ModalInteractionCallbackAction reply(Modal modal) {
        return new ModalInteractionCallbackAction(
                InteractionCallbackType.MODAL,
                new InteractionModalResponse(modal.title(), modal.customId(), modal.components()),
                getInteraction().token(),
                getInteraction().id(),
                getBot()
        );
    }
}