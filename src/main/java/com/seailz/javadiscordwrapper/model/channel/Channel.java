package com.seailz.javadiscordwrapper.model.channel;

import com.seailz.javadiscordwrapper.model.channel.utils.ChannelType;
import com.seailz.javadiscordwrapper.model.permission.PermissionOverwrite;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class Channel {

    private String id;
    private ChannelType type;
    private String guildId;
    private int position;
    private PermissionOverwrite[] permissionOverwrites;
    private String name;
    private String topic;
    private boolean nsfw;
    private String lastMessageId;
    private String parentId;
    private String lastPinTimestamp;
    private String permissions;
    private int defaultThreadRateLimitPerUser;

    public Channel(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, String topic, boolean nsfw, String lastMessageId, String parentId, String lastPinTimestamp, String permissions, int defaultThreadRateLimitPerUser) {
        this.id = id;
        this.type = type;
        this.guildId = guildId;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.name = name;
        this.topic = topic;
        this.nsfw = nsfw;
        this.lastMessageId = lastMessageId;
        this.parentId = parentId;
        this.lastPinTimestamp = lastPinTimestamp;
        this.permissions = permissions;
        this.defaultThreadRateLimitPerUser = defaultThreadRateLimitPerUser;
    }

    @NonNull
    public static Channel decompile(JSONObject json) {
        String id;
        ChannelType type;
        String guildId;
        int position;
        PermissionOverwrite[] permissionOverwrites;
        String name;
        String topic;
        boolean nsfw;
        String lastMessageId;
        String parentId;
        String lastPinTimestamp;
        String permissions;
        int defaultThreadRateLimitPerUser;

        try {
            id = json.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            type = ChannelType.fromCode(json.getInt("type"));
        } catch (JSONException e) {
            type = null;
        }

        try {
            guildId = json.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        try {
            position = json.getInt("position");
        } catch (JSONException e) {
            position = 0;
        }

        try {
            JSONArray overwrites = json.getJSONArray("permission_overwrites");
            ArrayList<PermissionOverwrite> overwritesList = new ArrayList<>();
            for (int i = 0; i < overwrites.length(); i++) {
                overwritesList.add(PermissionOverwrite.decompile(overwrites.getJSONObject(i)));
            }
            permissionOverwrites = overwritesList.toArray(new PermissionOverwrite[0]);
        } catch (JSONException e) {
            permissionOverwrites = null;
        }

        try {
            name = json.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            topic = json.getString("topic");
        } catch (JSONException e) {
            topic = null;
        }

        try {
            nsfw = json.getBoolean("nsfw");
        } catch (JSONException e) {
            nsfw = false;
        }

        try {
            lastMessageId = json.getString("last_message_id");
        } catch (JSONException e) {
            lastMessageId = null;
        }

        try {
            parentId = json.getString("parent_id");
        } catch (JSONException e) {
            parentId = null;
        }

        try {
            lastPinTimestamp = json.getString("last_pin_timestamp");
        } catch (JSONException e) {
            lastPinTimestamp = null;
        }

        try {
            permissions = json.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        try {
            defaultThreadRateLimitPerUser = json.getInt("default_thread_rate_limit_per_user");
        } catch (JSONException e) {
            defaultThreadRateLimitPerUser = 0;
        }

        return new Channel(id, type, guildId, position, permissionOverwrites, name, topic, nsfw, lastMessageId, parentId, lastPinTimestamp, permissions, defaultThreadRateLimitPerUser);

    }

    public String id() {
        return id;
    }

    public ChannelType type() {
        return type;
    }

    public String guildId() {
        return guildId;
    }

    public int position() {
        return position;
    }

    public PermissionOverwrite[] permissionOverwrites() {
        return permissionOverwrites;
    }

    public String name() {
        return name;
    }

    public String topic() {
        return topic;
    }

    public boolean nsfw() {
        return nsfw;
    }

    public String lastMessageId() {
        return lastMessageId;
    }

    public String parentId() {
        return parentId;
    }

    public String lastPinTimestamp() {
        return lastPinTimestamp;
    }

    public String permissions() {
        return permissions;
    }

    public int defaultThreadRateLimitPerUser() {
        return defaultThreadRateLimitPerUser;
    }

}
