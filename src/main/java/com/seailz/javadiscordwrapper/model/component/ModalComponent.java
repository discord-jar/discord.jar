package com.seailz.javadiscordwrapper.model.component;

/**
 * Component which can be inserted into a Modal TODO: add link to modal
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData
 */
public interface ModalComponent extends ActionComponent {

    @Override
    default boolean isMessageCompatible() {
        return type().isMessageCompatible();
    }

    @Override
    default boolean isModalCompatible() {
        return true;
    }

    @Override
    default int maxPerRow() {
        return 1;
    }

    boolean required();

    ModalComponent setRequired(boolean required);
}
