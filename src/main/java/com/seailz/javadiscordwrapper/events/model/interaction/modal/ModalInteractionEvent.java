package com.seailz.javadiscordwrapper.events.model.interaction.modal;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.interaction.InteractionEvent;
import com.seailz.javadiscordwrapper.model.component.ModalComponent;
import com.seailz.javadiscordwrapper.model.interaction.data.modal.ModalSubmitInteractionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class ModalInteractionEvent extends InteractionEvent {
    public ModalInteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction data that was created from the event.
     *
     * This SHOULD not ever return null.
     * @return {@link com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public ModalSubmitInteractionData getInteractionData() {
        return (ModalSubmitInteractionData) getInteraction().data();
    }

    /**
     * Returns the custom id of the {@link com.seailz.javadiscordwrapper.model.interaction.modal.Modal}.
     *
     * This SHOULD not ever return null.
     * @return {@link String} object containing the custom id.
     */
    @NotNull
    public String getCustomId() {
        return getInteractionData().customId();
    }

    /**
     * Returns the inputted components of the {@link com.seailz.javadiscordwrapper.model.interaction.modal.Modal}.
     *
     * This SHOULD not ever return null.
     * @return {@link String} object containing the values.
     */
    @NotNull
    public List<ModalComponent> getValues() {
        return getInteractionData().components();
    }

}
