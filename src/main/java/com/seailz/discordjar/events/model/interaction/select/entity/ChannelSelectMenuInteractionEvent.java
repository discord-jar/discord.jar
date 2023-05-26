package com.seailz.discordjar.events.model.interaction.select.entity;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.interaction.ModalInteractionCallbackAction;
import com.seailz.discordjar.events.DiscordListener;
import com.seailz.discordjar.events.model.interaction.CustomIdable;
import com.seailz.discordjar.events.model.interaction.InteractionEvent;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu;
import com.seailz.discordjar.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData;
import com.seailz.discordjar.model.interaction.modal.Modal;
import com.seailz.discordjar.model.interaction.reply.InteractionModalResponse;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.registry.components.ChannelSelectRegistry;
import com.seailz.discordjar.utils.registry.components.StringSelectRegistry;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Fires when a {@link ChannelSelectMenu} is used.
 * This includes all the data that is sent with the event. <p>
 * <p>
 * To implement this event, you must override the {@link DiscordListener#onChannelSelectMenuInteraction(ChannelSelectMenuInteractionEvent)} method in your {@link DiscordListener } class and then register it in your main class with {@link DiscordJar#registerListeners(DiscordListener...)}
 *
 * @author Seailz
 * @see <a href="https://discord.com/developers/docs/interactions/message-components#select-menus">Select Menu Documentation</a>
 * @see com.seailz.discordjar.model.component.select.SelectMenu
 * @see com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu
 * @since 1.0
 */
public class ChannelSelectMenuInteractionEvent extends InteractionEvent implements CustomIdable {

    public ChannelSelectMenuInteractionEvent(DiscordJar bot, long sequence, JSONObject data) {
        super(bot, sequence, data);

        ChannelSelectRegistry.getInstance().getRegistry().stream()
                .filter(select -> select.menu().customId().equals(getCustomId()))
                .forEach(select -> select.action().accept(this));
    }

    /**
     * Returns the interaction data that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData} object containing the interaction data.
     */
    @NotNull
    public MessageComponentInteractionData getInteractionData() {
        return (MessageComponentInteractionData) getInteraction().data();
    }


    /**
     * Returns the selected channels of the {@link com.seailz.discordjar.model.component.select.entity.ChannelSelectMenu ChannelSelectMenu}.
     *
     * @return A list of {@link Channel} objects containing the selected channels.
     * @throws IllegalStateException if the event was not fied in a {@link com.seailz.discordjar.model.guild.Guild Guild}.
     */
    public List<Channel> getSelectedChannels() {
        if (getGuild() == null)
            throw new IllegalArgumentException("This event was not fired in a guild.");

        List<Channel> channels = new ArrayList<>();
        getInteractionData().snowflakes().stream()
                .map(getBot()::getChannelById)
                .forEach(channels::add);
        return channels;
    }

    /**
     * Returns the custom id of the select menu.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link String} object containing the custom id.
     */
    @NotNull
    @Override
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

    public ModalInteractionCallbackAction replyModal(Modal modal) {
        return new ModalInteractionCallbackAction(
                InteractionCallbackType.MODAL,
                new InteractionModalResponse(modal.title(), modal.customId(), modal.components()),
                getInteraction().token(),
                getInteraction().id(),
                getBot()
        );
    }
}
