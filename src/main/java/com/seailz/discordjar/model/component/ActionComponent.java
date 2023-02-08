package com.seailz.discordjar.model.component;

/**
 * A component which supports interactions
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData
 * @since 1.0
 */
public interface ActionComponent extends RawComponent {

    /**
     * @return The custom id of the component
     */
    String customId();

}
