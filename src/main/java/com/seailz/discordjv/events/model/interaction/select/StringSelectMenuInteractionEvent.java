package com.seailz.discordjv.events.model.interaction.select;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.DiscordListener;
import com.seailz.discordjv.events.model.interaction.InteractionEvent;
import com.seailz.discordjv.model.component.select.SelectOption;
import com.seailz.discordjv.model.component.select.string.StringSelectMenu;
import com.seailz.discordjv.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.discordjv.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

/**
 * Fires when a {@link StringSelectMenu} is used.
 * This includes all the data that is sent with the event. <p>
 * <p>
 * To implement this event, you must override the {@link DiscordListener#onStringSelectMenuInteraction(StringSelectMenuInteractionEvent)} method in your {@link DiscordListener } class and then register it in your main class with {@link DiscordJv#registerListeners(DiscordListener...)}
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/interactions/message-components#select-menus">Select Menu Documentation</a>
 * @see com.seailz.discordjv.model.component.select.SelectMenu
 * @see com.seailz.discordjv.model.component.select.string.StringSelectMenu
 * @since 1.0
 */
public class StringSelectMenuInteractionEvent extends InteractionEvent {

    public StringSelectMenuInteractionEvent(DiscordJv bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction data that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link com.seailz.discordjv.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public MessageComponentInteractionData getInteractionData() {
        return (MessageComponentInteractionData) getInteraction().data();
    }

    /**
     * Returns the selected option of the {@link StringSelectMenuInteractionEvent}.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return A list of {@link SelectOption.ResolvedSelectOption} objects containing the selected options.
     */
    @NotNull
    public List<SelectOption.ResolvedSelectOption> getSelectedOptions() {
        return List.of(getInteractionData().selectOptions());
    }

    /**
     * Returns the custom id of the select menu.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link String} object containing the custom id.
     */
    @NotNull
    public String getCustomId() {
        return getInteractionData().customId();
    }

    /**
     * Returns the user who clicked the select menu. This SHOULD not ever return null.
     *
     * @return {@link User} object containing the user who clicked the select menu.
     */
    @NotNull
    public User getUser() {
        if (getInteraction().user() == null)
            return getInteraction().member().user();
        return getInteraction().user();
    }
}
