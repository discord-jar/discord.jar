package com.seailz.discordjar.model.component;

import com.seailz.discordjar.model.component.select.SelectMenu;
import com.seailz.discordjar.model.interaction.modal.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a component that is used to layout other {@link RawComponent RawComponents}, such as {@link SelectMenu SelectMenus}
 */
public interface DisplayComponent extends Component {

    /**
     * A {@link List} of {@link RawComponent RowComponents} that are displayed in the {@link DisplayComponent}
     *
     * @return A list of {@link RawComponent RawComponents}
     */
    @NotNull
    List<RawComponent> components();

    /**
     * Whether all components in the {@link DisplayComponent} are compatible with a {@link com.seailz.discordjar.model.message.Message Message}
     */
    default boolean isMessageCompatible() {
        if (!type().isMessageCompatible())
            return false;

        return components().stream().allMatch(RawComponent::isMessageCompatible);
    }

    /**
     * Whether all components in the {@link DisplayComponent} are compatible with a @{@link Modal}
     */
    default boolean isModalCompatible() {
        if (!type().isModalCompatible())
            return false;

        return components().stream().allMatch(RawComponent::isModalCompatible);
    }

    /**
     * Validates the {@link DisplayComponent} to ensure that it is compatible with Discord
     *
     * @return A boolean representing whether the {@link DisplayComponent} is valid
     */
    default boolean isValid() {
        for (RawComponent component : components()) {
            if (component.maxPerRow() > component.type().getMaxPerRow())
                return false;
        }

        return !components().isEmpty();
    }

    default void addComponent(RawComponent component) {
        components().add(component);
    }

    default void removeComponent(RawComponent component) {
        components().remove(component);
    }

    default void removeComponent(int index) {
        components().remove(index);
    }

    default void clearComponents() {
        components().clear();
    }

    default void setComponents(List<RawComponent> components) {
        components().clear();
        components().addAll(components);
    }

    default void setComponents(RawComponent... components) {
        components().clear();
        for (RawComponent component : components) {
            components().add(component);
        }
    }

    default void addComponents(List<RawComponent> components) {
        components().addAll(components);
    }

    default void addComponents(RawComponent... components) {
        for (RawComponent component : components) {
            components().add(component);
        }
    }
}
