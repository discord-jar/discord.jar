package com.seailz.discordjv.model.component;

import org.json.JSONException;

/**
 * Represents a component that can be used in a {@link com.seailz.discordjv.model.message.Message Message}
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.component.ActionComponent
 * @since 1.0
 */
public interface MessageComponent extends ActionComponent {

    @Override
    default boolean isModalCompatible() {
        return false;
    }

    @Override
    default boolean isMessageCompatible() {
        return true;
    }

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
     *
     * @param disabled If the component should be disabled
     * @return An {@link ActionComponent}
     */
    ActionComponent setDisabled(boolean disabled);

}
