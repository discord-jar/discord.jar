package com.seailz.javadiscordwrapper.action;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.component.DisplayComponent;
import com.seailz.javadiscordwrapper.model.interaction.callback.InteractionCallbackType;
import com.seailz.javadiscordwrapper.model.interaction.reply.InteractionMessageResponse;
import com.seailz.javadiscordwrapper.model.message.Attachment;

import java.util.List;

/**
 * Used to react to an interaction with a message.
 * This is an internal class to react to an interaction and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class MessageInteractionCallbackAction extends InteractionCallbackAction {
    public MessageInteractionCallbackAction(InteractionCallbackType type, InteractionMessageResponse reply, String token, String id, DiscordJv discordJv) {
        super(type, reply, token, id, discordJv);
    }

    /**
     * Enables or disables an ephemeral message.
     * @param ephemeral Whether the message should be ephemeral or not.
     */
    public MessageInteractionCallbackAction setEphemeral(boolean ephemeral) {
        ((InteractionMessageResponse) this.getReply()).setEphemeral(ephemeral);
        return this;
    }

    /**
     * Enables or disables TTS for the message.
     * @param tts Whether the message should be TTS or not.
     */
    public MessageInteractionCallbackAction setTTS(boolean tts) {
        ((InteractionMessageResponse) this.getReply()).setTts(tts);
        return this;
    }

    /**
     * Sets the content of the message.
     * @param content The content of the message.
     */
    public MessageInteractionCallbackAction setContent(String content) {
        ((InteractionMessageResponse) this.getReply()).setContent(content);
        return this;
    }

    /**
     * Sets the {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponents} of the message.
     * @param components The components of the message.
     */
    public MessageInteractionCallbackAction setComponents(List<DisplayComponent> components) {
        ((InteractionMessageResponse) this.getReply()).setComponents(components);
        return this;
    }

    /**
     * Adds a {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponent} to the message.
     * @param component The component to add
     */
    public MessageInteractionCallbackAction addComponent(DisplayComponent component) {
        ((InteractionMessageResponse) this.getReply()).addComponent(component);
        return this;
    }

    /**
     * Removes a {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponent} from the message.
     * @param component The component to remove
     */
    public MessageInteractionCallbackAction removeComponent(DisplayComponent component) {
        ((InteractionMessageResponse) this.getReply()).removeComponent(component);
        return this;
    }

    /**
     * Sets the {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponents} of the message.
     * @param components The components of the message.
     */
    public MessageInteractionCallbackAction setComponents(DisplayComponent... components) {
        return setComponents(List.of(components));
    }

    /**
     * Adds {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponents} to the message.
     * @param components The components to add
     */
    public MessageInteractionCallbackAction addComponents(List<DisplayComponent> components) {
        components.forEach(this::addComponent);
        return this;
    }

    /**
     * Adds {@link com.seailz.javadiscordwrapper.model.component.DisplayComponent DisplayComponents} to the message.
     * @param components The components to add
     */
    public MessageInteractionCallbackAction addComponents(DisplayComponent... components) {
        return addComponents(List.of(components));
    }

    /**
     * Will remove all embeds from the message
     * @param suppress Whether the embeds should be removed or not
     */
    public MessageInteractionCallbackAction suppressEmbeds(boolean suppress) {
        ((InteractionMessageResponse) this.getReply()).setSuppressEmbeds(suppress);
        return this;
    }

    // attachments

    public MessageInteractionCallbackAction setAttachments(Attachment... attachments) {
        ((InteractionMessageResponse) this.getReply()).setAttachments(List.of(attachments));
        return this;
    }

    public MessageInteractionCallbackAction setAttachments(List<Attachment> attachments) {
        ((InteractionMessageResponse) this.getReply()).setAttachments(attachments);
        return this;
    }

    // embeds
}
