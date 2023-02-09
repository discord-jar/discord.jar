package com.seailz.discordjar.action.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.ForumChannel;
import com.seailz.discordjar.model.channel.audio.VideoQualityMode;
import com.seailz.discordjar.model.channel.forum.DefaultReaction;
import com.seailz.discordjar.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjar.model.channel.forum.ForumTag;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.rest.DiscordRequest;
import com.seailz.discordjar.rest.DiscordResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModifyBaseChannelAction {

    private String channelId;
    private String name;
    private ChannelType type;
    private String topic;
    private int position = -1;
    private Boolean nsfw;
    private int bitrate = -1;
    private int rateLimitPerUser = -1;
    private int userLimit = -1;
    private List<PermissionOverwrite> permissionOverwrites;
    private Category parent;
    private String rtcRegion;
    private VideoQualityMode videoQualityMode;
    private String defaultAutoArchiveDuration;
    private int flags = -1;
    private List<ForumTag> availableTags;
    private DefaultReaction defaultReactionEmoji;
    private int defaultThreadRateLimitPerUser;
    private DefaultSortOrder defaultSortOrder;
    private ForumChannel.DefaultForumLayout defaultForumLayout;
    private DiscordJar djv;

    public ModifyBaseChannelAction(DiscordJar djv, String channelId, String name, ChannelType type, String topic, int position, boolean nsfw, int bitrate, int rateLimitPerUser, int userLimit, List<PermissionOverwrite> permissionOverwrites, Category parent, String rtcRegion, VideoQualityMode videoQualityMode, String defaultAutoArchiveDuration, int flags, List<ForumTag> availableTags, DefaultReaction defaultReactionEmoji, int defaultThreadRateLimitPerUser, DefaultSortOrder defaultSortOrder, ForumChannel.DefaultForumLayout defaultForumLayout) {
        this.name = name;
        this.type = type;
        this.topic = topic;
        this.position = position;
        this.nsfw = nsfw;
        this.bitrate = bitrate;
        this.rateLimitPerUser = rateLimitPerUser;
        this.userLimit = userLimit;
        this.permissionOverwrites = permissionOverwrites;
        this.parent = parent;
        this.rtcRegion = rtcRegion;
        this.videoQualityMode = videoQualityMode;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.flags = flags;
        this.availableTags = availableTags;
        this.defaultReactionEmoji = defaultReactionEmoji;
        this.defaultThreadRateLimitPerUser = defaultThreadRateLimitPerUser;
        this.defaultSortOrder = defaultSortOrder;
        this.channelId = channelId;
        this.djv = djv;
        this.defaultForumLayout = defaultForumLayout;
    }

    public ModifyBaseChannelAction(DiscordJar djv, String channelId) {
        this.channelId = channelId;
        this.djv = djv;
    }

    public String name() {
        return name;
    }

    public ChannelType type() {
        return type;
    }

    public String topic() {
        return topic;
    }

    public int position() {
        return position;
    }

    public boolean nsfw() {
        return nsfw;
    }

    public int bitrate() {
        return bitrate;
    }

    public int rateLimitPerUser() {
        return rateLimitPerUser;
    }

    public int userLimit() {
        return userLimit;
    }

    public List<PermissionOverwrite> permissionOverwrites() {
        return permissionOverwrites;
    }

    public Category parent() {
        return parent;
    }

    public String rtcRegion() {
        return rtcRegion;
    }

    public VideoQualityMode videoQualityMode() {
        return videoQualityMode;
    }

    public String defaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    public int flags() {
        return flags;
    }

    public List<ForumTag> availableTags() {
        return availableTags;
    }

    public DefaultReaction defaultReactionEmoji() {
        return defaultReactionEmoji;
    }

    public int defaultThreadRateLimitPerUser() {
        return defaultThreadRateLimitPerUser;
    }

    public DefaultSortOrder defaultSortOrder() {
        return defaultSortOrder;
    }

    public ForumChannel.DefaultForumLayout defaultForumLayout() {
        return defaultForumLayout;
    }

    public ModifyBaseChannelAction setName(String name) {
        this.name = name;
        return this;
    }

    public ModifyBaseChannelAction setType(ChannelType type) {
        this.type = type;
        return this;
    }

    public ModifyBaseChannelAction setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public ModifyBaseChannelAction setPosition(int position) {
        this.position = position;
        return this;
    }

    public ModifyBaseChannelAction setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public ModifyBaseChannelAction setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public ModifyBaseChannelAction setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
        return this;
    }

    public ModifyBaseChannelAction setUserLimit(int userLimit) {
        this.userLimit = userLimit;
        return this;
    }

    public ModifyBaseChannelAction setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
        return this;
    }

    public ModifyBaseChannelAction setParent(Category parent) {
        this.parent = parent;
        return this;
    }

    public ModifyBaseChannelAction setRtcRegion(String rtcRegion) {
        this.rtcRegion = rtcRegion;
        return this;
    }

    public ModifyBaseChannelAction setVideoQualityMode(VideoQualityMode videoQualityMode) {
        this.videoQualityMode = videoQualityMode;
        return this;
    }

    public ModifyBaseChannelAction setDefaultAutoArchiveDuration(String defaultAutoArchiveDuration) {
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        return this;
    }

    public ModifyBaseChannelAction setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ModifyBaseChannelAction setAvailableTags(List<ForumTag> availableTags) {
        this.availableTags = availableTags;
        return this;
    }

    public ModifyBaseChannelAction setDefaultReactionEmoji(DefaultReaction defaultReactionEmoji) {
        this.defaultReactionEmoji = defaultReactionEmoji;
        return this;
    }

    public ModifyBaseChannelAction setDefaultThreadRateLimitPerUser(int defaultThreadRateLimitPerUser) {
        this.defaultThreadRateLimitPerUser = defaultThreadRateLimitPerUser;
        return this;
    }

    public ModifyBaseChannelAction setDefaultSortOrder(DefaultSortOrder defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
        return this;
    }

    public void setDefaultForumLayout(ForumChannel.DefaultForumLayout defaultForumLayout) {
        this.defaultForumLayout = defaultForumLayout;
    }

    public CompletableFuture<Channel> run() {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            JSONObject body = new JSONObject();
            if (name != null) body.put("name", name);
            if (type != null) body.put("type", type.getCode());
            if (topic != null) body.put("topic", topic);
            if (position != -1) body.put("position", position);
            if (nsfw != null) body.put("nsfw", nsfw.booleanValue());
            if (bitrate != -1) body.put("bitrate", bitrate);
            if (rateLimitPerUser != -1) body.put("rate_limit_per_user", rateLimitPerUser);
            if (userLimit != -1) body.put("user_limit", userLimit);
            if (permissionOverwrites != null) {
                JSONArray array = new JSONArray();
                for (PermissionOverwrite overwrite : permissionOverwrites) {
                    array.put(overwrite.compile());
                }
                body.put("permission_overwrites", array);
            }
            if (parent != null) body.put("parent_id", parent.id());
            if (rtcRegion != null) body.put("rtc_region", rtcRegion);
            if (videoQualityMode != null) body.put("video_quality_mode", videoQualityMode.code());
            if (defaultAutoArchiveDuration != null) body.put("default_auto_archive_duration", defaultAutoArchiveDuration);
            if (flags != -1) body.put("flags", flags);
            if (availableTags != null) {
                JSONArray array = new JSONArray();
                for (ForumTag tag : availableTags) {
                    array.put(tag.compile());
                }
                body.put("available_tags", array);
            }
            if (defaultReactionEmoji != null) body.put("default_auto_archive_duration", defaultReactionEmoji.compile());
            if (defaultThreadRateLimitPerUser != -1) body.put("default_thread_rate_limit_per_user", defaultThreadRateLimitPerUser);
            if (defaultSortOrder != null) body.put("default_sort_order", defaultSortOrder.getCode());
            if (defaultForumLayout != null) body.put("default_forum_layout", defaultForumLayout.getCode());

            DiscordResponse response = new DiscordRequest(
                    body,
                    new HashMap<>(),
                    URLS.PATCH.CHANNEL.MODIFY_CHANNEL.replace("{channel.id}", channelId),
                    djv,
                    URLS.PATCH.CHANNEL.MODIFY_CHANNEL,
                    RequestMethod.PATCH
            ).invoke();

            return Channel.decompile(response.body(), djv);
        });
        return future;
    }

}
