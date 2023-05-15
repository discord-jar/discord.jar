package com.seailz.discordjar.action.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.message.MessageFlag;
import com.seailz.discordjar.model.message.MessageReference;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
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
 * Used to edit a message and define extra properties
 * This is an internal class that is used to edit a message & should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.message.Message
 * @since 1.0
 */
public class MessageEditAction {

    private String text;
    private List<Embeder> embeds;
    private List<DisplayComponent> components;
    private List<Attachment> attachments;
    private List<File> fileUploads;
    private boolean supressEmbeds;
    private final String channelId;
    private final DiscordJar discordJar;
    private final String messageId;

    public MessageEditAction(@NotNull String channelId, @NotNull DiscordJar discordJar, String messageId, boolean isVoiceMessage) {
        this.channelId = channelId;
        this.discordJar = discordJar;
        this.messageId = messageId;
        if (isVoiceMessage) throw new IllegalArgumentException("Cannot edit a voice message");
    }

    public MessageEditAction(ArrayList<DisplayComponent> components, @NotNull String channelId, @NotNull DiscordJar discordJar, String messageId) {
        this.components = components;
        this.channelId = channelId;
        this.discordJar = discordJar;
        this.messageId = messageId;
    }

    public MessageEditAction(List<Embeder> embeds, @NotNull String channelId, @NotNull DiscordJar discordJar, String messageId) {
        this.embeds = embeds;
        this.channelId = channelId;
        this.discordJar = discordJar;
        this.messageId = messageId;
    }

    public MessageEditAction(LinkedList<Attachment> attachments, @NotNull String channelId, @NotNull DiscordJar discordJar, String messageId) {
        this.attachments = attachments;
        this.channelId = channelId;
        this.discordJar = discordJar;
        this.messageId = messageId;
        this.components = new ArrayList<>();
        this.attachments = new ArrayList<>();
    }

    public MessageEditAction(@Nullable String text, @Nullable List<DisplayComponent> components, @Nullable List<Attachment> attachments, boolean supressEmbeds, @NotNull String channelId, @NotNull DiscordJar discordJar, String messageId) {
        this.text = text;
        this.components = components;
        this.attachments = attachments;
        this.supressEmbeds = supressEmbeds;
        this.channelId = channelId;
        this.discordJar = discordJar;
        this.messageId = messageId;
    }

    public String text() {
        return text;
    }

    public List<DisplayComponent> components() {
        return components;
    }

    public List<Attachment> attachments() {
        return attachments;
    }

    public boolean supressEmbeds() {
        return supressEmbeds;
    }

    public MessageEditAction setText(@Nullable String text) {
        this.text = text;
        return this;
    }

    public MessageEditAction setComponents(@Nullable List<DisplayComponent> components) {
        this.components = components;
        return this;
    }

    public MessageEditAction setAttachments(@Nullable List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public MessageEditAction setSupressEmbeds(boolean supressEmbeds) {
        this.supressEmbeds = supressEmbeds;
        return this;
    }

    public MessageEditAction addComponent(DisplayComponent component) {
        if (this.components == null)
            this.components = new ArrayList<>();
        this.components.add(component);
        return this;
    }

    public MessageEditAction addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public MessageEditAction addComponents(DisplayComponent... components) {
        if (this.components == null)
            this.components = new ArrayList<>();
        this.components.addAll(List.of(components));
        return this;
    }

    public MessageEditAction addEmbed(Embeder embed) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.add(embed);
        return this;
    }

    public MessageEditAction addEmbeds(Embeder... embeds) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.addAll(List.of(embeds));
        return this;
    }

    public MessageEditAction addEmbeds(List<Embeder> embeds) {
        if (this.embeds == null)
            this.embeds = new ArrayList<>();
        this.embeds.addAll(embeds);
        return this;
    }

    public MessageEditAction addFile(File file) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.add(file);
        return this;
    }

    public MessageEditAction addFiles(File... files) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.addAll(List.of(files));
        return this;
    }

    public MessageEditAction addFiles(List<File> files) {
        if (this.fileUploads == null)
            this.fileUploads = new ArrayList<>();
        this.fileUploads.addAll(files);
        return this;
    }

    public CompletableFuture<Message> run() {
        CompletableFuture<Message> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            String url = URLS.PATCH.CHANNEL.MESSAGE.EDIT.replace("{channel.id}", channelId).replace("{message.id}", messageId);

            JSONObject payload = new JSONObject();
            if (this.text != null) payload.put("content", this.text);

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

            if (this.attachments != null) {
                JSONArray files = new JSONArray();
                for (Attachment attachment : this.attachments) {
                    files.put(attachment.compile());
                }
                payload.put("attachments", files);
            }

            List<MessageFlag> flags = new ArrayList<>();
            if (this.supressEmbeds) flags.add(MessageFlag.SUPPRESS_EMBEDS);

            int flagsInt = 0;
            for (MessageFlag flag : flags) {
                flagsInt |= flag.getLeftShiftId();
            }
            if (flagsInt != 0)
                payload.put("flags", flagsInt);

            DiscordRequest request = new DiscordRequest(
                    payload,
                    new HashMap<>(),
                    url,
                    discordJar,
                    URLS.PATCH.CHANNEL.MESSAGE.EDIT,
                    RequestMethod.PATCH
            );

            DiscordResponse response = null;
            if (fileUploads != null && !fileUploads.isEmpty())
                response = request.invokeWithFiles(new ArrayList<>(fileUploads).toArray(new File[0]));
            else {
                try {
                    response = request.invoke();
                } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                    future.completeExceptionally(e);
                    return null;
                }
            }
            return Message.decompile(response.body(), discordJar);
        });
        return future;
    }

}
