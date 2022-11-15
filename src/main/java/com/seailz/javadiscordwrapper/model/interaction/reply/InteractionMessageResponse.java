package com.seailz.javadiscordwrapper.model.interaction.reply;

import com.seailz.javadiscordwrapper.model.component.DisplayComponent;
import com.seailz.javadiscordwrapper.model.message.Attachment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link com.seailz.javadiscordwrapper.model.interaction.callback.InteractionCallbackType InteractionCallbackType} 4 interaction response.
 * This includes some message info.
 *
 * Note: This is an internal class and should not be used by the end user.
 * @author Seailz
 * @since  1.0
 * @see    InteractionReply
 */
public class InteractionMessageResponse implements InteractionReply {

    private boolean tts;
    private String content;
    //todo: embeds
    //todo: allows mentions
    private boolean ephemeral;
    private boolean suppressEmbeds;
    private List<DisplayComponent> components;
    private List<Attachment> attachments;

    public InteractionMessageResponse(String content) {
        this.content = content;
    }

    public InteractionMessageResponse(
            boolean tts,
            String content,
            boolean ephemeral,
            boolean suppressEmbeds,
            List<DisplayComponent> components,
            List<Attachment> attachments
    ) {
        this.tts = tts;
        this.content = content;
        this.ephemeral = ephemeral;
        this.suppressEmbeds = suppressEmbeds;
        this.components = components;
        this.attachments = attachments;
    }

    /**
     * Adds a component to the message response
     *
     * NOTE: This only supports {@link DisplayComponent DisplayComponents} at the moment.
     */
    public void addComponent(DisplayComponent component) {
        if (this.components == null)
            this.components = new ArrayList<>();
        this.components.add(component);
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void setComponents(List<DisplayComponent> components) {
        this.components = components;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSuppressEmbeds(boolean suppressEmbeds) {
        this.suppressEmbeds = suppressEmbeds;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    /**
     * Compiles this class up to be sent to Discord
     */
    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
    }
}
