package com.seailz.discordjv.events.model.interaction;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.interaction.MessageInteractionCallbackAction;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.model.embed.Embeder;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.interaction.Interaction;
import com.seailz.discordjv.model.interaction.InteractionData;
import com.seailz.discordjv.model.interaction.InteractionType;
import com.seailz.discordjv.model.interaction.callback.InteractionCallbackType;
import com.seailz.discordjv.model.interaction.reply.InteractionMessageResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents an interaction event
 * This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see CommandInteractionEvent
 * @since 1.0
 */
public class InteractionEvent extends Event {
    public InteractionEvent(@NotNull DiscordJv bot, long sequence, @NotNull JSONObject data) {
        super(bot, sequence, data);
    }

    /**
     * Returns the interaction that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link Interaction} object containing the interaction data.
     */
    @NotNull
    public Interaction getInteraction() {
        try {
            return Interaction.decompile(getJson().getJSONObject("d"), getBot());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the type of interaction that was created from the event.
     * <p>
     * This SHOULD not ever return null.
     *
     * @return {@link InteractionType}
     */
    @NotNull
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
    public InteractionData getInteractionData() {
        return getInteraction().data();
    }

    @NotNull
    public MessageInteractionCallbackAction reply(String message) {
        return new MessageInteractionCallbackAction(InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE, new InteractionMessageResponse(message), getInteraction().token(), getInteraction().id(), getBot());
    }

    @NotNull
    public MessageInteractionCallbackAction replyWithEmbeds(Embeder... embeds) {
        return new MessageInteractionCallbackAction(InteractionCallbackType.CHANNEL_MESSAGE_WITH_SOURCE, new InteractionMessageResponse(embeds), getInteraction().token(), getInteraction().id(), getBot());
    }
}
