package com.seailz.discordjv.action.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.channel.audio.VideoQualityMode;
import com.seailz.discordjv.model.channel.forum.DefaultReaction;
import com.seailz.discordjv.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjv.model.channel.forum.ForumTag;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
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
    private int position;
    private Boolean nsfw;
    private int bitrate;
    private int rateLimitPerUser;
    private int userLimit;
    private List<PermissionOverwrite> permissionOverwrites;
    private Category parent;
    private String rtcRegion;
    private VideoQualityMode videoQualityMode;
    private String defaultAutoArchiveDuration;
    private int flags;
    private List<ForumTag> availableTags;
    private DefaultReaction defaultReactionEmoji;
    private int defaultThreadRateLimitPerUser;
    private DefaultSortOrder defaultSortOrder;
    private DiscordJv djv;

    public ModifyBaseChannelAction(DiscordJv djv, String channelId, String name, ChannelType type, String topic, int position, boolean nsfw, int bitrate, int rateLimitPerUser, int userLimit, List<PermissionOverwrite> permissionOverwrites, Category parent, String rtcRegion, VideoQualityMode videoQualityMode, String defaultAutoArchiveDuration, int flags, List<ForumTag> availableTags, DefaultReaction defaultReactionEmoji, int defaultThreadRateLimitPerUser, DefaultSortOrder defaultSortOrder) {
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
    }

    public ModifyBaseChannelAction(DiscordJv djv, String channelId) {
        this.channelId = channelId;
        this.djv = djv;
    }

    private String name() {
        return name;
    }

    private ChannelType type() {
        return type;
    }

    private String topic() {
        return topic;
    }

    private int position() {
        return position;
    }

    private boolean nsfw() {
        return nsfw;
    }

    private int bitrate() {
        return bitrate;
    }

    private int rateLimitPerUser() {
        return rateLimitPerUser;
    }

    private int userLimit() {
        return userLimit;
    }

    private List<PermissionOverwrite> permissionOverwrites() {
        return permissionOverwrites;
    }

    private Category parent() {
        return parent;
    }

    private String rtcRegion() {
        return rtcRegion;
    }

    private VideoQualityMode videoQualityMode() {
        return videoQualityMode;
    }

    private String defaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    private int flags() {
        return flags;
    }

    private List<ForumTag> availableTags() {
        return availableTags;
    }

    private DefaultReaction defaultReactionEmoji() {
        return defaultReactionEmoji;
    }

    private int defaultThreadRateLimitPerUser() {
        return defaultThreadRateLimitPerUser;
    }

    private DefaultSortOrder defaultSortOrder() {
        return defaultSortOrder;
    }

    private ModifyBaseChannelAction setName(String name) {
        this.name = name;
        return this;
    }

    private ModifyBaseChannelAction setType(ChannelType type) {
        this.type = type;
        return this;
    }

    private ModifyBaseChannelAction setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    private ModifyBaseChannelAction setPosition(int position) {
        this.position = position;
        return this;
    }

    private ModifyBaseChannelAction setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    private ModifyBaseChannelAction setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    private ModifyBaseChannelAction setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
        return this;
    }

    private ModifyBaseChannelAction setUserLimit(int userLimit) {
        this.userLimit = userLimit;
        return this;
    }

    private ModifyBaseChannelAction setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
        return this;
    }

    private ModifyBaseChannelAction setParent(Category parent) {
        this.parent = parent;
        return this;
    }

    private ModifyBaseChannelAction setRtcRegion(String rtcRegion) {
        this.rtcRegion = rtcRegion;
        return this;
    }

    private ModifyBaseChannelAction setVideoQualityMode(VideoQualityMode videoQualityMode) {
        this.videoQualityMode = videoQualityMode;
        return this;
    }

    private ModifyBaseChannelAction setDefaultAutoArchiveDuration(String defaultAutoArchiveDuration) {
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        return this;
    }

    private ModifyBaseChannelAction setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    private ModifyBaseChannelAction setAvailableTags(List<ForumTag> availableTags) {
        this.availableTags = availableTags;
        return this;
    }

    private ModifyBaseChannelAction setDefaultReactionEmoji(DefaultReaction defaultReactionEmoji) {
        this.defaultReactionEmoji = defaultReactionEmoji;
        return this;
    }

    private ModifyBaseChannelAction setDefaultThreadRateLimitPerUser(int defaultThreadRateLimitPerUser) {
        this.defaultThreadRateLimitPerUser = defaultThreadRateLimitPerUser;
        return this;
    }

    private ModifyBaseChannelAction setDefaultSortOrder(DefaultSortOrder defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
        return this;
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
