package com.seailz.discordjar.model.interaction.reply;

import com.seailz.discordjar.action.interaction.InteractionCallbackAction;
import com.seailz.discordjar.core.Compilerable;

/**
 * Represents a reply to an interaction.
 * This could include things such as a modal, or a message.
 * <p>
 * This class itself ({@link InteractionReply}) is not a functional class and is used
 * to mark interaction replies.
 * <p>
 * A list of responses can be found <a href="https://discord.com/developers/docs/interactions/receiving-and-responding#interaction-response-object-interaction-callback-data-structure">here</a>
 * A list of responses supported in discord.jar can be found in this package.
 *
 * @author Seailz
 * @see com.seailz.discordjar.events.model.interaction.InteractionEvent
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @see InteractionCallbackAction
 * @since 1.0
 */
public interface InteractionReply extends Compilerable {
}
