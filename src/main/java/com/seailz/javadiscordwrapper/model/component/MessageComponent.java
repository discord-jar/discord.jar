package com.seailz.javadiscordwrapper.model.component;

import org.json.JSONException;

/**
 * Represents a component that can be used in a {@link com.seailz.javadiscordwrapper.model.message.Message Message}
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.component.ActionComponent
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
     * @param disabled If the component should be disabled
     * @return An {@link ActionComponent}
     */
    ActionComponent setDisabled(boolean disabled);

}
