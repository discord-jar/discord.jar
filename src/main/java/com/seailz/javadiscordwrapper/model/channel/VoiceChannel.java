package com.seailz.javadiscordwrapper.model.channel;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.channel.utils.ChannelType;
import com.seailz.javadiscordwrapper.model.message.Message;
import com.seailz.javadiscordwrapper.model.permission.PermissionOverwrite;
import com.seailz.javadiscordwrapper.model.video.VideoQualityMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class VoiceChannel extends Channel implements Compilerable {

    private int userLimit;
    private int rateLimitPerUser;
    private int bitrate;
    private int rtcRegion;
    private VideoQualityMode videoQualityMode;

    public VoiceChannel(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, String topic, boolean nsfw, String lastMessageId, int rateLimitPerUser, String parentId, String lastPinTimestamp, String permissions, int defaultThreadRateLimitPerUser, int userLimit, int bitrate, int rtcRegion, VideoQualityMode videoQualityMode) {
        super(id, type, guildId, position, permissionOverwrites, name, topic, nsfw, lastMessageId, parentId, lastPinTimestamp, permissions, defaultThreadRateLimitPerUser);
        this.userLimit = userLimit;
        this.rateLimitPerUser = rateLimitPerUser;
        this.bitrate = bitrate;
        this.rtcRegion = rtcRegion;
        this.videoQualityMode = videoQualityMode;
    }


    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("user_limit", userLimit)
                .put("rate_limit_per_user", rateLimitPerUser)
                .put("bitrate", bitrate)
                .put("rtc_region", rtcRegion)
                .put("video_quality_mode", videoQualityMode.getCode())
                .put("type", type().getCode())
                .put("name", name())
                .put("topic", topic())
                .put("nsfw", nsfw())
                .put("parent_id", parentId())
                .put("last_pin_timestamp", lastPinTimestamp())
                .put("permissions", permissions())
                .put("default_auto_archive_duration", defaultThreadRateLimitPerUser());
    }

    @NonNull
    public static VoiceChannel decompile(JSONObject obj) {
        int userLimit;
        int rateLimitPerUser;
        int bitrate;
        int rtcRegion;
        VideoQualityMode videoQualityMode;
        ChannelType type;
        String name;
        String topic;
        boolean nsfw;
        String parentId;
        String lastPinTimestamp;
        String permissions;
        int defaultThreadRateLimitPerUser;
        String id;
        int position;
        PermissionOverwrite[] permissionOverwrites;
        String guildId;

        try {
            position = obj.getInt("position");
        } catch (JSONException e) {
            position = 0;
        }

        try {
            position = obj.getInt("position");
        } catch (JSONException e) {
            position = 0;
        }

        try {
            nsfw = obj.getBoolean("nsfw");
        } catch (JSONException e) {
            nsfw = false;
        }

        try {
            ArrayList<PermissionOverwrite> permissionOverwritesDecompiled = new ArrayList<>();
            JSONArray overwriteArray = obj.getJSONArray("permission_overwrites");
            for (int i = 0; i < overwriteArray.length(); i++) {
                permissionOverwritesDecompiled.add(PermissionOverwrite.decompile(overwriteArray.getJSONObject(i)));
            }

            permissionOverwrites = permissionOverwritesDecompiled.toArray(new PermissionOverwrite[0]);
        } catch (JSONException e) {
            permissionOverwrites = new PermissionOverwrite[0];
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            userLimit = obj.getInt("user_limit");
        } catch (JSONException e) {
            userLimit = 0;
        }

        try {
            rateLimitPerUser = obj.getInt("rate_limit_per_user");
        } catch (JSONException e) {
            rateLimitPerUser = 0;
        }

        try {
            bitrate = obj.getInt("bitrate");
        } catch (JSONException e) {
            bitrate = 0;
        }

        try {
            rtcRegion = obj.getInt("rtc_region");
        } catch (JSONException e) {
            rtcRegion = 0;
        }

        try {
            videoQualityMode = VideoQualityMode.fromCode(obj.getInt("video_quality_mode"));
        } catch (JSONException e) {
            videoQualityMode = VideoQualityMode.AUTO;
        }

        try {
            type = ChannelType.fromCode(obj.getInt("type"));
        } catch (JSONException e) {
            type = ChannelType.GUILD_TEXT;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = "";
        }

        try {
            topic = obj.getString("topic");
        } catch (JSONException e) {
            topic = "";
        }

        try {
            nsfw = obj.getBoolean("nsfw");
        } catch (JSONException e) {
            nsfw = false;
        }

        try {
            parentId = obj.getString("parent_id");
        } catch (JSONException e) {
            parentId = "";
        }

        try {
            lastPinTimestamp = obj.getString("last_pin_timestamp");
        } catch (JSONException e) {
            lastPinTimestamp = "";
        }

        try {
            permissions = obj.getString("permissions");
        } catch (JSONException e) {
            permissions = "";
        }

        try {
            defaultThreadRateLimitPerUser = obj.getInt("default_auto_archive_duration");
        } catch (JSONException e) {
            defaultThreadRateLimitPerUser = 0;
        }

        return new VoiceChannel(id, type, guildId, position, permissionOverwrites, name, topic, nsfw, null, rateLimitPerUser, parentId, lastPinTimestamp, permissions, defaultThreadRateLimitPerUser, userLimit, bitrate, rtcRegion, videoQualityMode);

    }
}
