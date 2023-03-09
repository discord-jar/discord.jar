package com.seailz.discordjar.model.interaction.reply;

import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.mentions.AllowedMentions;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.MessageFlag;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a {@link com.seailz.discordjar.model.interaction.callback.InteractionCallbackType InteractionCallbackType} 4 interaction response.
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
    private List<Embeder> embeds;
    private AllowedMentions allowedMentions;
    private boolean ephemeral;
    private boolean suppressEmbeds;
    private boolean silent;
    private List<DisplayComponent> components;
    private List<Attachment> attachments;

    public InteractionMessageResponse(String content) {
        this.content = content;
    }

    public InteractionMessageResponse(Embeder... embeds) {
        this.embeds = Arrays.asList(embeds);
    }

    public InteractionMessageResponse() {
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

    public void setEmbeds(List<Embeder> embeds) {
        this.embeds = embeds;
    }

    public void setAllowedMentions(AllowedMentions allowedMentions) {
        this.allowedMentions = allowedMentions;
    }

    public AllowedMentions getAllowedMentions() {
        return allowedMentions;
    }

    public void addEmbed(Embeder embed) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.add(embed);
    }

    public void removeComponent(DisplayComponent component) {
        if (this.components == null)
            return;
        this.components.remove(component);
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public boolean isSilent() {
        return silent;
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

        if (this.silent)
            flags.add(MessageFlag.SUPPRESS_NOTICICATIONS);

        if (this.embeds != null) {
            List<JSONObject> embeds = new ArrayList<>();
            for (Embeder embed : this.embeds) {
                embeds.add(embed.compile());
            }
            obj.put("embeds", embeds);
        }

        int flagsInt = 0;
        for (MessageFlag flag : flags) {
            flagsInt |= flag.getLeftShiftId();
        }
        if (flagsInt != 0)
            obj.put("flags", flagsInt);


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

        if (this.allowedMentions != null)
            obj.put("allowed_mentions", this.allowedMentions.compile());

        return obj;
    }
}
