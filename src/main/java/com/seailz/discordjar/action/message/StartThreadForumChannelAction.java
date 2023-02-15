package com.seailz.discordjar.action.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.thread.Thread;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.message.MessageFlag;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class StartThreadForumChannelAction {

    private final String channelId;
    private final String name;
    private final ForumThreadMessageParams message;
    private final DiscordJar djar;

    private ArchiveDuration autoArchiveDuration;
    private int rateLimitPerUser;
    private final List<String> tagIds = new ArrayList<>();


    public StartThreadForumChannelAction(String channelId, String name, ForumThreadMessageParams message, DiscordJar djar) {
        this.channelId = channelId;
        this.name = name;
        this.message = message;
        this.djar = djar;
    }

    public StartThreadForumChannelAction setAutoArchiveDuration(ArchiveDuration autoArchiveDuration) {
        this.autoArchiveDuration = autoArchiveDuration;
        return this;
    }

    public StartThreadForumChannelAction setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
        return this;
    }

    public ArchiveDuration autoArchiveDuration() {
        return autoArchiveDuration;
    }

    public ForumThreadMessageParams message() {
        return message;
    }

    public int slowmode() {
        return rateLimitPerUser;
    }

    public List<String> tagIds() {
        return tagIds;
    }

    public String channelId() {
        return channelId;
    }

    public String getName() {
        return name;
    }

    public StartThreadForumChannelAction addTagId(String tagId) {
        tagIds.add(tagId);
        return this;
    }

    public StartThreadForumChannelAction addTagIds(List<String> tagIds) {
        this.tagIds.addAll(tagIds);
        return this;
    }

    public StartThreadForumChannelAction removeTagId(String tagId) {
        tagIds.remove(tagId);
        return this;
    }

    public StartThreadForumChannelAction removeTagIds(List<String> tagIds) {
        this.tagIds.removeAll(tagIds);
        return this;
    }

    public StartThreadForumChannelAction clearTagIds() {
        tagIds.clear();
        return this;
    }

    public StartThreadForumChannelAction setTagIds(List<String> tagIds) {
        this.tagIds.clear();
        this.tagIds.addAll(tagIds);
        return this;
    }

    public StartThreadForumChannelAction setTagIds(String... tagIds) {
        this.tagIds.clear();
        this.tagIds.addAll(Arrays.asList(tagIds));
        return this;
    }


    public CompletableFuture<Thread> run() {
        CompletableFuture<Thread> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            JSONObject payload = new JSONObject();
            payload.put("name", name);
            if (autoArchiveDuration != null) {
                payload.put("auto_archive_duration", autoArchiveDuration.getMinutes());
            }

            if (rateLimitPerUser != 0) {
                payload.put("rate_limit_per_user", rateLimitPerUser);
            }

            if (!tagIds.isEmpty()) {
                JSONArray tagIdsArray = new JSONArray();
                tagIds.forEach(tagIdsArray::put);
                payload.put("applied_tags", tagIdsArray);
            }

            payload.put("message", message.compile());

            DiscordRequest request = new DiscordRequest(
                    payload,
                    new HashMap<>(),
                    URLS.POST.MESSAGES.START_THREAD_FORUM.replace("{channel.id}", channelId),
                    djar,
                    URLS.POST.MESSAGES.START_THREAD_FORUM,
                    RequestMethod.POST
            );
            return Thread.decompile(
                    request.invoke().body(), djar
            );
        });
        return future;
    }

    public enum ArchiveDuration {
        MINUTES_60(60),
        MINUTES_1440(1440),
        MINUTES_4320(4320),
        MINUTES_10080(10080);

        private final int minutes;

        ArchiveDuration(int minutes) {
            this.minutes = minutes;
        }

        public int getMinutes() {
            return minutes;
        }
    }

    public static class ForumThreadMessageParams {
        private String text;
        private List<Embeder> embeds;
        // TODO: allowed mentions
        private List<DisplayComponent> components;
        private List<String> stickerIds;
        private List<Attachment> attachments;
        private List<File> fileUploads;
        private boolean supressEmbeds;
        private boolean silent = false;

        public ForumThreadMessageParams(@Nullable String text) {
            this.text = text;
        }

        public ForumThreadMessageParams(ArrayList<DisplayComponent> components) {
            this.components = components;
        }

        public ForumThreadMessageParams(List<Embeder> embeds) {
            this.embeds = embeds;
        }

        public ForumThreadMessageParams(LinkedList<Attachment> attachments) {
            this.attachments = attachments;
            this.components = new ArrayList<>();
            this.attachments = new ArrayList<>();
            this.stickerIds = new ArrayList<>();
        }

        public ForumThreadMessageParams(@Nullable String text, @Nullable List<DisplayComponent> components, @Nullable List<String> stickerIds, @Nullable List<Attachment> attachments, boolean supressEmbeds) {
            this.text = text;
            this.components = components;
            this.stickerIds = stickerIds;
            this.attachments = attachments;
            this.supressEmbeds = supressEmbeds;
        }

        public String text() {
            return text;
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

        public ForumThreadMessageParams setText(@Nullable String text) {
            this.text = text;
            return this;
        }

        public ForumThreadMessageParams setComponents(@Nullable List<DisplayComponent> components) {
            this.components = components;
            return this;
        }

        public ForumThreadMessageParams setStickerIds(@Nullable List<String> stickerIds) {
            this.stickerIds = stickerIds;
            return this;
        }

        public ForumThreadMessageParams setAttachments(@Nullable List<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public ForumThreadMessageParams setSupressEmbeds(boolean supressEmbeds) {
            this.supressEmbeds = supressEmbeds;
            return this;
        }

        public ForumThreadMessageParams addComponent(DisplayComponent component) {
            if (this.components == null)
                this.components = new ArrayList<>();
            this.components.add(component);
            return this;
        }

        public ForumThreadMessageParams addStickerId(String stickerId) {
            this.stickerIds.add(stickerId);
            return this;
        }

        public ForumThreadMessageParams addAttachment(Attachment attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public ForumThreadMessageParams addComponents(DisplayComponent... components) {
            if (this.components == null)
                this.components = new ArrayList<>();
            this.components.addAll(List.of(components));
            return this;
        }

        public ForumThreadMessageParams addEmbed(Embeder embed) {
            if (this.embeds == null)
                this.embeds = new ArrayList<>();
            this.embeds.add(embed);
            return this;
        }

        public ForumThreadMessageParams addEmbeds(Embeder... embeds) {
            if (this.embeds == null)
                this.embeds = new ArrayList<>();
            this.embeds.addAll(List.of(embeds));
            return this;
        }

        public ForumThreadMessageParams addEmbeds(List<Embeder> embeds) {
            if (this.embeds == null)
                this.embeds = new ArrayList<>();
            this.embeds.addAll(embeds);
            return this;
        }

        public ForumThreadMessageParams addFile(File file) {
            if (this.fileUploads == null)
                this.fileUploads = new ArrayList<>();
            this.fileUploads.add(file);
            return this;
        }

        public ForumThreadMessageParams addFiles(File... files) {
            if (this.fileUploads == null)
                this.fileUploads = new ArrayList<>();
            this.fileUploads.addAll(List.of(files));
            return this;
        }

        public ForumThreadMessageParams addFiles(List<File> files) {
            if (this.fileUploads == null)
                this.fileUploads = new ArrayList<>();
            this.fileUploads.addAll(files);
            return this;
        }

        public void setSilent(boolean silent) {
            this.silent = silent;
        }

        public boolean isSilent() {
            return silent;
        }

        protected JSONObject compile() {
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

            List<MessageFlag> flags = new ArrayList<>();
            if (this.supressEmbeds) flags.add(MessageFlag.SUPPRESS_EMBEDS);
            if (this.silent) flags.add(MessageFlag.SUPPRESS_NOTICICATIONS);

            int flagsInt = 0;
            for (MessageFlag flag : flags) {
                flagsInt |= flag.getLeftShiftId();
            }
            if (flagsInt != 0)
                payload.put("flags", flagsInt);
            return payload;
        }

    }
}
