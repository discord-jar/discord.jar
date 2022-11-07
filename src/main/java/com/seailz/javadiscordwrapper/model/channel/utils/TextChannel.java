package com.seailz.javadiscordwrapper.model.channel.utils;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class TextChannel extends Channel implements Compilerable {

    private String lastMessageId;
    private String lastPinTimestamp;
    private int defaultThreadRateLimitPerUser;
    private String topic;

    public TextChannel(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, boolean nsfw, String parentId, String permissions) {
        super(id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, permissions);
    }

    @Override
    public JSONObject compile() {
       return new JSONObject()
                .put("id", id())
                .put("type", type().getCode())
                .put("guild_id", guildId())
                .put("position", position())
                .put("permission_overwrites", permissionOverwrites())
                .put("name", name())
                .put("topic", topic)
                .put("nsfw", nsfw())
                .put("last_message_id", lastMessageId)
                .put("parent_id", parentId())
                .put("last_pin_timestamp", lastPinTimestamp)
                .put("permissions", permissions())
                .put("default_auto_archive_duration", defaultThreadRateLimitPerUser);
    }

    @NotNull
    public static TextChannel decompile(JSONObject obj) {
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
            type = ChannelType.fromCode(obj.getInt("type"));
        } catch (JSONException e) {
            type = null;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            topic = obj.getString("topic");
        } catch (JSONException e) {
            topic = null;
        }

        try {
            nsfw = obj.getBoolean("nsfw");
        } catch (JSONException e) {
            nsfw = false;
        }

        try {
            parentId = obj.getString("parent_id");
        } catch (JSONException e) {
            parentId = null;
        }

        try {
            lastPinTimestamp = obj.getString("last_pin_timestamp");
        } catch (JSONException e) {
            lastPinTimestamp = null;
        }

        try {
            permissions = obj.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        try {
            defaultThreadRateLimitPerUser = obj.getInt("default_auto_archive_duration");
        } catch (JSONException e) {
            defaultThreadRateLimitPerUser = 0;
        }

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            position = obj.getInt("position");
        } catch (JSONException e) {
            position = 0;
        }

        try {
            JSONArray array = obj.getJSONArray("permission_overwrites");
            permissionOverwrites = new PermissionOverwrite[array.length()];
            for (int i = 0; i < array.length(); i++) {
                permissionOverwrites[i] = PermissionOverwrite.decompile(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            permissionOverwrites = null;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        return new TextChannel(id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, permissions);
    }

    public String lastMessageId() {
        return lastMessageId;
    }

    public void lastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }
}
