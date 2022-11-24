package com.seailz.discordjv.events.model.interaction.button;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.interaction.ModalInteractionCallbackAction;
import com.seailz.discordjv.events.model.interaction.InteractionEvent;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.discordjv.model.interaction.modal.Modal;
import com.seailz.discordjv.model.interaction.reply.InteractionModalResponse;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ButtonInteractionEvent extends InteractionEvent {
    public ButtonInteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction data that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link com.seailz.discordjv.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
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
