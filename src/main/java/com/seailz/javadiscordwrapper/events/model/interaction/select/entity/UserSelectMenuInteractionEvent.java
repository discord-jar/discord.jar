package com.seailz.javadiscordwrapper.events.model.interaction.select.entity;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.model.interaction.InteractionEvent;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import com.seailz.javadiscordwrapper.model.component.select.string.StringSelectMenu;
import com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.javadiscordwrapper.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserSelectMenuInteractionEvent extends InteractionEvent {

    public UserSelectMenuInteractionEvent(DiscordJv bot, long sequence, JSONObject data) {
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
     * Returns the selected option of the {@link UserSelectMenuInteractionEvent}.
     *
     * This SHOULD not ever return null.
     * @return A list of {@link SelectOption} objects containing the selected options.
     */
    @NotNull
    public List<SelectOption> getSelectedOptions() {
        return List.of(getInteractionData().selectOptions());
    }

    /**
     * Returns the selected users of the {@link com.seailz.javadiscordwrapper.model.component.select.entitiy.UserSelectMenu UserSelectMenu}.
     *
     * @throws IllegalStateException if the event was not fied in a {@link com.seailz.javadiscordwrapper.model.guild.Guild Guild}.
     *
     * @return A list of {@link User} objects containing the selected users.
     */
    public List<User> getSelectedUsers() {
        List<User> returnList = new ArrayList<>();
        for (String s : getInteractionData().snowflakes()) {
           returnList.add(getBot().getUserById(s));
        }
        return returnList;
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
    public UserSelectMenuInteractionEvent getComponent() {
        return (UserSelectMenuInteractionEvent) getInteraction().message().components().stream()
                .filter(component -> component instanceof UserSelectMenuInteractionEvent)
                .filter(component -> ((UserSelectMenuInteractionEvent) component).getCustomId().equals(getCustomId()))
                .findFirst()
                .orElseThrow();
    }
}

