package com.seailz.javadiscordwrapper.model.component;

import org.json.JSONException;

/**
 * A component which supports interactions
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData
 */
public interface ActionComponent extends RawComponent {

    /**
     * @return The custom id of the component
     */
    String customId();

}
