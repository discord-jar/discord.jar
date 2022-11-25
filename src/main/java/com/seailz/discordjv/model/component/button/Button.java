package com.seailz.discordjv.model.component.button;

import com.seailz.discordjv.model.component.MessageComponent;
import com.seailz.discordjv.model.component.button.internal.ButtonImpl;
import com.seailz.discordjv.model.emoji.Emoji;

public interface Button extends MessageComponent {

    String label();

    ButtonStyle style();

    Emoji emoji();

    boolean isDisabled();

    String url();

    Button setDisabled(boolean disabled);

    Button setCustomId(String customId);

    Button setLabel(String label);

    Button setStyle(ButtonStyle style);

    Button setEmoji(Emoji emoji);

    Button setUrl(String url);

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


}
