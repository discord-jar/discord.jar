package com.seailz.javadiscordwrapper.events.model.interaction;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.action.interaction.MessageInteractionCallbackAction;
import com.seailz.javadiscordwrapper.events.model.Event;
import com.seailz.javadiscordwrapper.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.model.interaction.Interaction;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import com.seailz.javadiscordwrapper.model.interaction.InteractionType;
import com.seailz.javadiscordwrapper.model.interaction.callback.InteractionCallbackType;
import com.seailz.javadiscordwrapper.model.interaction.reply.InteractionMessageResponse;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Represents an interaction event
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    CommandInteractionEvent
 */
public class InteractionEvent extends Event {
    public InteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction that was created from the event.
     *
     * This SHOULD not ever return null.
     * @return {@link Interaction} object containing the interaction data.
     */
    @NotNull
    @SneakyThrows
    public Interaction getInteraction() {
        return Interaction.decompile(getJson().getJSONObject("d"), getBot());
    }

    /**
     * Returns the type of interaction that was created from the event.
     *
     * This SHOULD not ever return null.
     * @return {@link InteractionType}
     */
    @NotNull
    @SneakyThrows
    public InteractionType getInteractionType() {
        return getInteraction().type();
    }

    /**
     * Returns the guild that the interaction was created in.
     * This will return null if the interaction was not created in a guild, and in a DM.
     *
     * @return {@link Guild} object containing the guild data.
     */
    @Nullable
    @SneakyThrows
    public Guild getGuild() {
        return getInteraction().guild();
    }

    /**
     * Returns the interaction data that was created from the event.
     * This can be used by the end user, but is not recommended.
     *
     * <p>Note: Will return null if {@link #getInteractionType()} is {@link InteractionType#PING}</p>
     *
     * @return {@link InteractionData} object containing the interaction data.
     */
    @Nullable
    @SneakyThrows
    public InteractionData getInteractionData() {
        return getInteraction().data();
    }

    @NotNull
    public MessageInteractionCallbackAction reply(String message) {
        return new MessageInteractionCallbackAction(InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE, new InteractionMessageResponse(message), getInteraction().token(), getInteraction().id(), getBot());
    }
}
