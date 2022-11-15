package com.seailz.javadiscordwrapper.events.model.interaction.select.entity;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.interaction.InteractionEvent;
import com.seailz.javadiscordwrapper.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import com.seailz.javadiscordwrapper.model.component.select.string.StringSelectMenu;
import com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.javadiscordwrapper.model.role.Role;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class RoleSelectMenuInteractionEvent extends InteractionEvent {

    public RoleSelectMenuInteractionEvent(DiscordJv bot, long sequence, JSONObject data) {
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
     * Returns the selected option of the {@link com.seailz.javadiscordwrapper.model.component.select.entitiy.RoleSelectMenu RoleSelectMenu}.
     *
     * This SHOULD not ever return null.
     * @return A list of {@link SelectOption} objects containing the selected options.
     */
    @NotNull
    public List<SelectOption> getSelectedOptions() {
        return List.of(getInteractionData().selectOptions());
    }

    /**
     * Returns the selected roles of the {@link com.seailz.javadiscordwrapper.model.component.select.entitiy.RoleSelectMenu RoleSelectMenu}.
     *
     * @throws IllegalStateException if the event was not fied in a {@link com.seailz.javadiscordwrapper.model.guild.Guild Guild}.
     *
     * @return A list of {@link Role} objects containing the selected roles.
     */
    public List<Role> getSelectedRoles() {
        if (getGuild() == null)
            throw new IllegalArgumentException("This event was not fired in a guild.");

        return getGuild().roles().stream()
                .filter(role -> getInteractionData().snowflakes().stream()
                        .anyMatch(option -> option.equals(role.id())))
                .toList();
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
    public RoleSelectMenuInteractionEvent getComponent() {
        return (RoleSelectMenuInteractionEvent) getInteraction().message().components().stream()
                .filter(component -> component instanceof RoleSelectMenuInteractionEvent)
                .filter(component -> ((RoleSelectMenuInteractionEvent) component).getCustomId().equals(getCustomId()))
                .findFirst()
                .orElseThrow();
    }
}
