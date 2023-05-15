package com.seailz.discordjar.events.model.interaction.button;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.interaction.ModalInteractionCallbackAction;
import com.seailz.discordjar.events.model.interaction.CustomIdable;
import com.seailz.discordjar.events.model.interaction.InteractionEvent;
import com.seailz.discordjar.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.discordjar.model.interaction.modal.Modal;
import com.seailz.discordjar.model.interaction.reply.InteractionModalResponse;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.registry.ButtonRegistry;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ButtonInteractionEvent extends InteractionEvent implements CustomIdable {
    public ButtonInteractionEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
        // First checks the button registry for any actions that match the custom id.
        ButtonRegistry.getInstance().getRegistry().stream()
                .filter(buttonAction -> buttonAction.button().customId().equals(getCustomId()))
                .forEach(buttonAction -> buttonAction.action().accept(this));
    }

    /**
     * Returns the interaction data that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public MessageComponentInteractionData getInteractionData() {
        return (MessageComponentInteractionData) getInteraction().data();
    }

    /**
     * Returns the custom id of the select menu.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link String} object containing the custom id.
     */
    @NotNull
    @Override
    public String getCustomId() {
        return getInteractionData().customId();
    }

    /**
     * Returns the user who clicked the button. This SHOULD not ever return null.
     *
     * @return {@link User} object containing the user who clicked the button.
     */
    @NotNull
    public User getUser() {
        if (getInteraction().user() == null)
            return getInteraction().member().user();
        return getInteraction().user();
    }

    /**
     * Replies to the interaction with a {@link Modal}.
     *
     * @param modal The modal to send.
     * @return {@link ModalInteractionCallbackAction} object containing the action to send the modal.
     */
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
