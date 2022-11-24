package com.seailz.discordjv.model.interaction.reply;

import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.message.Attachment;
import com.seailz.discordjv.model.message.MessageFlag;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link com.seailz.discordjv.model.interaction.callback.InteractionCallbackType InteractionCallbackType} 4 interaction response.
 * This includes some message info.
 * <p>
 * Note: This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see InteractionReply
 * @since 1.0
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
     * <p>
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

    public void removeComponent(DisplayComponent component) {
        if (this.components == null)
            return;
        this.components.remove(component);
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
        if (this.tts)
            obj.put("tts", true);

        if (this.content != null)
            obj.put("content", this.content);

        List<MessageFlag> flags = new ArrayList<>();
        if (this.ephemeral)
            flags.add(MessageFlag.EPHEMERAL);

        if (this.suppressEmbeds)
            flags.add(MessageFlag.SUPPRESS_EMBEDS);

        if (flags.size() == 1)
            obj.put("flags", flags.get(0).getLeftShiftId());

        if (flags.size() == 2)
            obj.put("flags", flags.get(0).getLeftShiftId() | flags.get(1).getLeftShiftId());

        if (this.components != null) {
            List<JSONObject> components = new ArrayList<>();
            for (DisplayComponent component : this.components) {
                components.add(component.compile());
            }
            obj.put("components", components);
        }

        if (this.attachments != null) {
            List<JSONObject> attachments = new ArrayList<>();
            for (Attachment attachment : this.attachments) {
                attachments.add(attachment.compile());
            }
            obj.put("attachments", attachments);
        }

        return obj;
    }
}
