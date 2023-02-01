package com.seailz.discordjv.model.component.button;

import com.seailz.discordjv.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjv.model.component.MessageComponent;
import com.seailz.discordjv.model.component.button.internal.ButtonImpl;
import com.seailz.discordjv.model.emoji.Emoji;

import java.util.function.Consumer;

/**
 * A button component.
 * <p>
 * These can be attached to messages, and when clicked, will send a {@link ButtonInteractionEvent}.
 * They can have different styles, and can be disabled.
 * They can also be link buttons, which will redirect the user to a URL when clicked.
 *
 * @author Seailz
 * @see ButtonInteractionEvent
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface Button extends MessageComponent {

    /**
     * Returns the label of the button.
     *
     * @return The label of the button.
     */
    String label();

    /**
     * Returns the style of the button. This determines the color of the button.
     *
     * @return The style of the button.
     */
    ButtonStyle style();

    /**
     * Returns the emoji of the button.
     *
     * @return The emoji of the button.
     */
    Emoji emoji();

    /**
     * Returns true if the button is disabled.
     *
     * @return True if the button is disabled.
     */
    boolean isDisabled();

    /**
     * Returns the URL of the button if it is a link button.
     *
     * @return The URL of the button.
     */
    String url();

    /**
     * Sets a button to disabled or not.
     *
     * @param disabled If the component should be disabled
     */
    Button setDisabled(boolean disabled);

    /**
     * Sets the custom id of the button.
     *
     * @param customId The custom you want to set.
     */
    Button setCustomId(String customId);

    /**
     * Sets the label of the button.
     *
     * @param label The label you want to set.
     */
    Button setLabel(String label);

    /**
     * Sets the style of the button.
     *
     * @param style The style you want to set.
     */
    Button setStyle(ButtonStyle style);

    /**
     * Sets the emoji of the button.
     *
     * @param emoji The emoji you want to set.
     */
    Button setEmoji(Emoji emoji);

    /**
     * Sets the URL of the button.
     *
     * @param url The URL you want to set.
     */
    Button setUrl(String url);

    /**
     * Sets the action of the button.
     *
     * @param action The action you want to set.
     */
    Button setAction(Consumer<ButtonInteractionEvent> action);

    /**
     * Returns a primary button
     *
     * @param label    The label of the button
     * @param customId The custom id of the button
     * @return {@link ButtonImpl}
     */
    static Button primary(String label, String customId) {
        return new ButtonImpl().setStyle(ButtonStyle.PRIMARY).setLabel(label).setCustomId(customId);
    }

    /**
     * Returns a secondary button
     *
     * @param label    The label of the button
     * @param customId The custom id of the button
     * @return {@link ButtonImpl}
     */
    static Button secondary(String label, String customId) {
        return new ButtonImpl().setStyle(ButtonStyle.SECONDARY).setLabel(label).setCustomId(customId);
    }

    /**
     * Returns a success button
     *
     * @param label    The label of the button
     * @param customId The custom id of the button
     * @return {@link ButtonImpl}
     */
    static Button success(String label, String customId) {
        return new ButtonImpl().setStyle(ButtonStyle.SUCCESS).setLabel(label).setCustomId(customId);
    }

    /**
     * Returns a danger button
     *
     * @param label    The label of the button
     * @param customId The custom id of the button
     * @return {@link ButtonImpl}
     */
    static Button danger(String label, String customId) {
        return new ButtonImpl().setStyle(ButtonStyle.DANGER).setLabel(label).setCustomId(customId);
    }

    /**
     * Returns a link button
     *
     * @param label    The label of the button
     * @param customId The custom id of the button
     * @return {@link ButtonImpl}
     */
    static Button link(String label, String customId, String url) {
        return new ButtonImpl().setStyle(ButtonStyle.LINK).setLabel(label).setUrl(url).setCustomId(customId);
    }


    record ButtonAction(Button button, Consumer<ButtonInteractionEvent> action) {
    }

}
