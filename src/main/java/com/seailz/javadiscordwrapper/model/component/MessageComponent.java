package com.seailz.javadiscordwrapper.model.component;

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

}
