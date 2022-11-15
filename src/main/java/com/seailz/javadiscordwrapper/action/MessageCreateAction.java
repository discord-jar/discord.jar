package com.seailz.javadiscordwrapper.action;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.action.finished.FinishedAction;
import com.seailz.javadiscordwrapper.model.component.DisplayComponent;
import com.seailz.javadiscordwrapper.model.component.MessageComponent;
import com.seailz.javadiscordwrapper.model.message.Attachment;
import com.seailz.javadiscordwrapper.model.message.Message;
import com.seailz.javadiscordwrapper.model.message.MessageFlag;
import com.seailz.javadiscordwrapper.model.message.MessageReference;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used to create a message and define extra properties
 * This is an internal class that is used to create a message & should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.message.Message
 */
public class MessageCreateAction {

    private String text;
    private String nonce;
    private boolean tts;
    // TODO: embeds
    // TODO: allowed mentions
    private MessageReference messageReference;
    private List<DisplayComponent> components;
    private List<String> stickerIds;
    // TODO: files
    // TODO: json payload
    private List<Attachment> attachments;
    private boolean supressEmbeds;
    private String channelId;
    private DiscordJv discordJv;

    public MessageCreateAction(@Nullable String text, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.text = text;
        this.channelId = channelId;
        this.discordJv = discordJv;
    }

    public MessageCreateAction(ArrayList<DisplayComponent> components, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.components = components;
        this.channelId = channelId;
        this.discordJv = discordJv;
    }

    public MessageCreateAction(List<Attachment> attachments, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.attachments = attachments;
        this.channelId = channelId;
        this.discordJv = discordJv;
    }

    public MessageCreateAction(@Nullable String text, @Nullable String nonce, boolean tts, @Nullable MessageReference messageReference, @Nullable List<DisplayComponent> components, @Nullable List<String> stickerIds, @Nullable List<Attachment> attachments, boolean supressEmbeds, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.text = text;
        this.nonce = nonce;
        this.tts = tts;
        this.messageReference = messageReference;
        this.components = components;
        this.stickerIds = stickerIds;
        this.attachments = attachments;
        this.supressEmbeds = supressEmbeds;
        this.channelId = channelId;
        this.discordJv = discordJv;
    }

    public String text() {
        return text;
    }

    public String nonce() {
        return nonce;
    }

    public boolean tts() {
        return tts;
    }

    public MessageReference messageReference() {
        return messageReference;
    }

    public List<DisplayComponent> components() {
        return components;
    }

    public List<String> stickerIds() {
        return stickerIds;
    }

    public List<Attachment> attachments() {
        return attachments;
    }

    public boolean supressEmbeds() {
        return supressEmbeds;
    }

    public MessageCreateAction setText(@Nullable String text) {
        this.text = text;
        return this;
    }

    public MessageCreateAction setNonce(@Nullable String nonce) {
        this.nonce = nonce;
        return this;
    }

    public MessageCreateAction setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    public MessageCreateAction setMessageReference(@Nullable MessageReference messageReference) {
        this.messageReference = messageReference;
        return this;
    }

    public MessageCreateAction setComponents(@Nullable List<DisplayComponent> components) {
        this.components = components;
        return this;
    }

    public MessageCreateAction setStickerIds(@Nullable List<String> stickerIds) {
        this.stickerIds = stickerIds;
        return this;
    }

    public MessageCreateAction setAttachments(@Nullable List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public MessageCreateAction setSupressEmbeds(boolean supressEmbeds) {
        this.supressEmbeds = supressEmbeds;
        return this;
    }

    public MessageCreateAction addComponent(DisplayComponent component) {
        this.components.add(component);
        return this;
    }

    public MessageCreateAction addStickerId(String stickerId) {
        this.stickerIds.add(stickerId);
        return this;
    }

    public MessageCreateAction addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public MessageCreateAction addComponents(DisplayComponent... components) {
        this.components.addAll(List.of(components));
        return this;
    }

    public FinishedAction<Message> run() {
        String url = URLS.POST.MESSAGES.SEND.replace("{channel.id}", channelId);

        JSONObject payload = new JSONObject();
        payload.put("content", text == null ? JSONObject.NULL : text);
        if (nonce != null)
            payload.put("nonce", nonce);

        payload.put("tts", tts);

        if (messageReference != null)
            payload.put("message_reference", messageReference.compile());

        JSONArray components = new JSONArray();
        if (this.components != null) {
            for (DisplayComponent component : this.components) {
                components.put(component.compile());
            }
        }

        if (this.components != null)
            payload.put("components", components);

        JSONArray stickerIds = new JSONArray();
        if (this.stickerIds != null) {
            for (String stickerId : this.stickerIds) {
                stickerIds.put(stickerId);
            }
        }
        if (this.stickerIds != null)
            payload.put("sticker_ids", stickerIds);

        JSONArray attachments = new JSONArray();
        if (this.attachments != null) {
            for (Attachment attachment : this.attachments) {
                attachments.put(attachment.compile());
            }
        }

        if (this.attachments != null)
            payload.put("attachments", attachments);

        if (this.supressEmbeds)
            payload.put("flags", MessageFlag.SUPPRESS_EMBEDS.getLeftShiftId());

        HashMap<String, String> headers = new HashMap<>();

        DiscordRequest request = new DiscordRequest(
                payload,
                headers,
                url,
                discordJv,
                URLS.POST.MESSAGES.SEND,
                RequestMethod.POST
        );

        DiscordResponse response = request.invoke();
        return new FinishedAction<>(Message.decompile(response.body(), discordJv));
    }

}
