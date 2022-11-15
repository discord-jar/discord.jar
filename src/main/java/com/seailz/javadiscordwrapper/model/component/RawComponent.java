package com.seailz.javadiscordwrapper.model.component;

/**
 * Component which can be inserted into a {@link DisplayComponent}
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData
 */
public interface RawComponent extends Component {

    /**
     * @return The max amount of this component that can be in a row
     */
    default int maxPerRow() {
        return type().getMaxPerRow();
    }

    /**
     * @return If this component is message compatible
     */
    @Override
    default boolean isMessageCompatible() {
        return type().isMessageCompatible();
    }

    /**
     * @return If this component is modal compatible
     */
    @Override
    default boolean isModalCompatible() {
        return type().isModalCompatible();
    }

}
