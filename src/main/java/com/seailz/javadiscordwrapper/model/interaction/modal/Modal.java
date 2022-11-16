package com.seailz.javadiscordwrapper.model.interaction.modal;

import com.seailz.javadiscordwrapper.model.component.DisplayComponent;
import com.seailz.javadiscordwrapper.model.interaction.reply.InteractionReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a modal
 * @param title The title of the modal
 * @param customId The custom id of the modal
 * @param components The components of the modal
 *
 * @author Seailz
 * @since  1.0
 * @see    InteractionReply
 */
public record Modal(
        String title,
        String customId,
        List<DisplayComponent> components
) {

    public Modal(String title, String customId, DisplayComponent... components) {
        this(title, customId, List.of(components));
    }

    public Modal(String title, String customId) {
        this(title, customId, new ArrayList<>());
    }

    public Modal addComponent(DisplayComponent component) {
        this.components().add(component);
        return this;
    }

    public Modal addComponents(DisplayComponent... components) {
        this.components().addAll(List.of(components));
        return this;
    }

    public Modal removeComponent(DisplayComponent component) {
        this.components().remove(component);
        return this;
    }

    public Modal removeComponents(DisplayComponent... components) {
        this.components().removeAll(List.of(components));
        return this;
    }
}
