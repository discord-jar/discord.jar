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

    /**
     * @return If the component is disabled
     */
    default boolean isDisabled() {
        try {
            return compile().getBoolean("disabled");
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Returns a disabled/non-disabled version of this component
     * @param disabled If the component should be disabled
     * @return An {@link ActionComponent}
     */
    ActionComponent setDisabled(boolean disabled);


}
