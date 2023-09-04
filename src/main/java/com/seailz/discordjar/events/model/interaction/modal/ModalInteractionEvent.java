package com.seailz.discordjar.events.model.interaction.modal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.DiscordListener;
import com.seailz.discordjar.events.model.interaction.CustomIdable;
import com.seailz.discordjar.events.model.interaction.InteractionEvent;
import com.seailz.discordjar.model.component.modal.ResolvedModalComponent;
import com.seailz.discordjar.model.interaction.data.modal.ModalSubmitInteractionData;
import com.seailz.discordjar.model.interaction.modal.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

/**
 * Fires when a {@link Modal} is used.
 * This includes all the data that is sent with the event. <p>
 * <p>
 * To implement this event, you must override the {@link DiscordListener#onModalInteractionEvent(ModalInteractionEvent)} method in your {@link DiscordListener } class and then register it in your main class with {@link DiscordJar#registerListeners(DiscordListener...)}
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-response-object-modal">Modal Documentation</a>
 * @see com.seailz.discordjar.model.component.ModalComponent
 * @see Modal
 * @since 1.0
 */
public class ModalInteractionEvent extends InteractionEvent implements CustomIdable {
    public ModalInteractionEvent(@NotNull DiscordJar bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction data that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public ModalSubmitInteractionData getInteractionData() {
        return (ModalSubmitInteractionData) getInteraction().data();
    }

    /**
     * Returns the custom id of the {@link com.seailz.discordjar.model.interaction.modal.Modal}.
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
     * Returns the inputted components of the {@link com.seailz.discordjar.model.interaction.modal.Modal}.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link String} object containing the values.
     */
    @NotNull
    public List<ResolvedModalComponent> getValues() {
        return getInteractionData().components();
    }

    /**
     * Given a custom ID, returns one of the inputted components of the {@link com.seailz.discordjar.model.interaction.modal.Modal} that matches the custom ID.
     * This will return null if no component matches the custom ID.
     *
     * @param customId The custom ID of the component.
     * @return {@link ResolvedModalComponent} object containing the component data.
     */
    @Nullable
    public ResolvedModalComponent getValue(String customId) {
        return getValues().stream().filter(resolvedModalComponent -> resolvedModalComponent.customId().equals(customId)).findFirst().orElse(null);
    }

}
