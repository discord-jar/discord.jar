package com.seailz.discordjv.action;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.embed.Embeder;
import com.seailz.discordjv.model.message.Attachment;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.model.message.MessageFlag;
import com.seailz.discordjv.model.message.MessageReference;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Used to create a message and define extra properties
 * This is an internal class that is used to create a message & should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.message.Message
 * @since 1.0
 */
public class MessageCreateAction {

    private String text;
    private String nonce;
    private boolean tts;
    private List<Embeder> embeds;
    // TODO: allowed mentions
    private MessageReference messageReference;
    private List<DisplayComponent> components;
    private List<String> stickerIds;
    // TODO: files
    // TODO: json payload
    private List<Attachment> attachments;
    private List<File> fileUploads;
    private boolean supressEmbeds;
    private final String channelId;
    private final DiscordJv discordJv;

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

    public MessageCreateAction(List<Embeder> embeds, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.embeds = embeds;
        this.channelId = channelId;
        this.discordJv = discordJv;
    }

    public MessageCreateAction(LinkedList<Attachment> attachments, @NotNull String channelId, @NotNull DiscordJv discordJv) {
        this.attachments = attachments;
        this.channelId = channelId;
        this.discordJv = discordJv;
        this.components = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.stickerIds = new ArrayList<>();
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
        if (this.components == null)
            this.components = new ArrayList<>();
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
        if (this.components == null)
            this.components = new ArrayList<>();
        this.components.addAll(List.of(components));
        return this;
    }

    public MessageCreateAction addEmbed(Embeder embed) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.add(embed);
        return this;
    }

    public MessageCreateAction addEmbeds(Embeder... embeds) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.addAll(List.of(embeds));
        return this;
    }

    public MessageCreateAction addEmbeds(List<Embeder> embeds) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.addAll(embeds);
        return this;
    }

    public MessageCreateAction addFile(File file) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.add(file);
        return this;
    }

    public MessageCreateAction addFiles(File... files) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.addAll(List.of(files));
        return this;
    }

    public MessageCreateAction addFiles(List<File> files) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.addAll(files);
        return this;
    }


    public CompletableFuture<Message> run() {
        CompletableFuture<Message> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            String url = URLS.POST.MESSAGES.SEND.replace("{channel.id}", channelId);

            JSONObject payload = new JSONObject();
            if (this.text != null) payload.put("content", this.text);
            if (this.nonce != null) payload.put("nonce", this.nonce);
            if (this.tts) payload.put("tts", true);
            if (this.messageReference != null) payload.put("message_reference", this.messageReference.compile());

            JSONArray components = new JSONArray();
            if (this.components != null && !this.components.isEmpty()) {
                for (DisplayComponent component : this.components) {
                    components.put(component.compile());
                }
            }

            if (this.components != null && !this.components.isEmpty())
                payload.put("components", components);

            JSONArray embeds = new JSONArray();
            if (this.embeds != null) {
                for (Embeder embed : this.embeds) {
                    embeds.put(embed.compile());
                }
            }

            if (this.embeds != null)
                payload.put("embeds", embeds);


            JSONArray stickerIds = new JSONArray();
            if (this.stickerIds != null) {
                for (String stickerId : this.stickerIds) {
                    stickerIds.put(stickerId);
                }
            }

            if (this.stickerIds != null && !this.stickerIds.isEmpty())
                payload.put("sticker_ids", stickerIds);

            if (this.attachments != null) {
                JSONArray files = new JSONArray();
                for (Attachment attachment : this.attachments) {
                    files.put(attachment.compile());
                }
                payload.put("attachments", files);
            }

            if (this.supressEmbeds) payload.put("flags", MessageFlag.SUPPRESS_EMBEDS.getLeftShiftId());

            DiscordRequest request = new DiscordRequest(
                    payload,
                    new HashMap<>(),
                    url,
                    discordJv,
                    URLS.POST.MESSAGES.SEND,
                    RequestMethod.POST
            );

            DiscordResponse response = null;
            if (fileUploads != null && !fileUploads.isEmpty())
                response = request.invokeWithFiles(new ArrayList<>(fileUploads).toArray(new File[0]));
            else
                request.invoke();
            return Message.decompile(response.body(), discordJv);
        });
        return future;
    }

    // todo: embeds

}
