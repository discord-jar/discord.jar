package com.seailz.discordjar.action.interaction;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EditInteractionMessageAction {

    private final String applicationId;
    private final String interactionToken;
    private final DiscordJar discordJar;

    private final boolean orig;
    private final String followupId;

    private String content;
    private List<Embeder> embeds;
    //TODO: allowed mentions
    private List<DisplayComponent> components;
    private List<File> fileUploads;
    private List<Attachment> attachments;


    public EditInteractionMessageAction(String applicationId, String interactionToken, DiscordJar discordJar, boolean orig, @Nullable String followupId) {
        this.applicationId = applicationId;
        this.interactionToken = interactionToken;
        this.discordJar = discordJar;
        this.orig = orig;
        this.followupId = followupId;

        this.components = new ArrayList<>();
        this.attachments = new ArrayList<>();
        this.fileUploads = new ArrayList<>();
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public List<DisplayComponent> getComponents() {
        return components;
    }

    public List<Embeder> getEmbeds() {
        return embeds;
    }

    public List<File> getFileUploads() {
        return fileUploads;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getContent() {
        return content;
    }

    public String getFollowupId() {
        return followupId;
    }

    public String getInteractionToken() {
        return interactionToken;
    }

    public EditInteractionMessageAction setContent(String content) {
        this.content = content;
        return this;
    }

    public EditInteractionMessageAction setComponents(List<DisplayComponent> components) {
        this.components = components;
        return this;
    }

    public EditInteractionMessageAction setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public EditInteractionMessageAction setEmbeds(List<Embeder> embeds) {
        this.embeds = embeds;
        return this;
    }

    public EditInteractionMessageAction setFileUploads(List<File> fileUploads) {
        this.fileUploads = fileUploads;
        return this;
    }

    public EditInteractionMessageAction addFileUpload(File file) {
        this.fileUploads.add(file);
        return this;
    }

    public EditInteractionMessageAction addEmbed(Embeder embed) {
        this.embeds.add(embed);
        return this;
    }

    public EditInteractionMessageAction addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        return this;
    }

    public EditInteractionMessageAction addComponent(DisplayComponent component) {
        this.components.add(component);
        return this;
    }

    public CompletableFuture<Message> run() {
        CompletableFuture<Message> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            JSONObject obj = new JSONObject();
            if (content != null) obj.put("content", content);
            if (embeds != null) {
                JSONArray embeds = new JSONArray();
                for (Embeder embed : this.embeds) {
                    embeds.put(embed.compile());
                }

                obj.put("embeds", embeds);
            }

            if (components != null) {
                JSONArray components = new JSONArray();
                for (DisplayComponent component : this.components) {
                    components.put(component.compile());
                }

                obj.put("components", components);
            }

            if (attachments != null) {
                JSONArray attachments = new JSONArray();
                for (Attachment attachment : this.attachments) {
                    attachments.put(attachment.compile());
                }

                obj.put("attachments", attachments);
            }

            DiscordRequest request = new DiscordRequest(
                    obj,
                    new HashMap<>(),
                    orig ?
                            URLS.PATCH.INTERACTIONS.MESSAGES.MODIFY_ORIGINAL_INTERACTION_RESPONSE
                                    .replace("{application.id}", applicationId)
                                    .replace("{interaction.token}", interactionToken) :
                            URLS.PATCH.INTERACTIONS.MESSAGES.MODIFY_FOLLOWUP_MESSAGE
                                    .replace("{application.id}", applicationId)
                                    .replace("{interaction.token}", interactionToken)
                                    .replace("{message.id}", followupId),
                    discordJar,
                    orig ?
                            URLS.PATCH.INTERACTIONS.MESSAGES.MODIFY_ORIGINAL_INTERACTION_RESPONSE :
                            URLS.PATCH.INTERACTIONS.MESSAGES.MODIFY_FOLLOWUP_MESSAGE,
                    RequestMethod.PATCH
            );

            return Message.decompile(request.invoke().body(), discordJar);
        });
        return future;
    }

}
