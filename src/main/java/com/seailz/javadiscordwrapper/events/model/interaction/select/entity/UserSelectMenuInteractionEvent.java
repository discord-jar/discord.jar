package com.seailz.javadiscordwrapper.events.model.interaction.select.entity;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.events.DiscordListener;
import com.seailz.javadiscordwrapper.events.model.interaction.InteractionEvent;
import com.seailz.javadiscordwrapper.events.model.interaction.select.StringSelectMenuInteractionEvent;
import com.seailz.javadiscordwrapper.model.component.select.entity.UserSelectMenu;
import com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.javadiscordwrapper.model.user.User;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fires when a {@link UserSelectMenu} is used.
 * This includes all the data that is sent with the event. <p>
 *
 * To implement this event, you must override the {@link DiscordListener#onUserSelectMenuInteraction(UserSelectMenuInteractionEvent)} method in your {@link DiscordListener } class and then register it in your main class with {@link DiscordJv#registerListeners(DiscordListener...)}
 *
 * @author Seailz
 * @since  1.0
 * @see    <a href="https://discord.com/developers/docs/interactions/message-components#select-menus">Select Menu Documentation</a>
 * @see    com.seailz.javadiscordwrapper.model.component.select.SelectMenu
 * @see    com.seailz.javadiscordwrapper.model.component.select.entity.UserSelectMenu
 */
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
     * Returns the selected users of the {@link com.seailz.javadiscordwrapper.model.component.select.entity.UserSelectMenu UserSelectMenu}.
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
     * Returns the user who clicked the select menu. This SHOULD not ever return null.
     * @return {@link User} object containing the user who clicked the select menu.
     */
    @NotNull
    public User getUser() {
        if (getInteraction().user() == null)
            return getInteraction().member().user();
        return getInteraction().user();
    }
}

