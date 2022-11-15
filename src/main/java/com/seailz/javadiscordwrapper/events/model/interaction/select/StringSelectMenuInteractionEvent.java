package com.seailz.javadiscordwrapper.events.model.interaction.select;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.interaction.InteractionEvent;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import com.seailz.javadiscordwrapper.model.component.select.string.StringSelectMenu;
import com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class StringSelectMenuInteractionEvent extends InteractionEvent {

    public StringSelectMenuInteractionEvent(DiscordJv bot, long sequence, JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction data that was created from the event.
     *
     * This SHOULD not ever return null.
     * @return {@link com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public MessageComponentInteractionData getInteractionData() {
        return (MessageComponentInteractionData) getInteraction().data();
    }

    /**
     * Returns the selected option of the {@link StringSelectMenuInteractionEvent}.
     *
     * This SHOULD not ever return null.
     * @return A list of {@link SelectOption} objects containing the selected options.
     */
    @NotNull
    public List<SelectOption> getSelectedOptions() {
        return List.of(getInteractionData().selectOptions());
    }

    /**
     * Returns the custom id of the select menu.
     *
     * This SHOULD not ever return null.
     * @return {@link String} object containing the custom id.
     */
    @NotNull
    public String getCustomId() {
        return getInteractionData().customId();
    }

    /**
     * Retrieves the {@link StringSelectMenu} component
     */
    @NotNull
    public StringSelectMenu getComponent() {
        return (StringSelectMenu) getInteraction().message().components().stream()
                .filter(component -> component instanceof StringSelectMenu)
                .filter(component -> ((StringSelectMenu) component).customId().equals(getCustomId()))
                .findFirst()
                .orElseThrow();
    }
}
