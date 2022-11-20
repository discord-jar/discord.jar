package com.seailz.javadiscordwrapper.model.channel;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.user.User;
import com.seailz.javadiscordwrapper.model.channel.utils.ChannelType;
import com.seailz.javadiscordwrapper.model.permission.PermissionOverwrite;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class GroupDM extends Channel implements Compilerable {

    private User[] recipients;
    private String icon;
    private String ownerId;
    private String applicationId;

    public GroupDM(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, String topic, boolean nsfw, String lastMessageId, String parentId, String lastPinTimestamp, String permissions, int defaultThreadRateLimitPerUser, User[] recipients, String icon, String ownerId, String applicationId, DiscordJv discordJv) {
        super(id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, permissions, discordJv);
        this.recipients = recipients;
        this.icon = icon;
        this.ownerId = ownerId;
        this.applicationId = applicationId;
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("recipients", recipients)
                .put("icon", icon)
                .put("owner_id", ownerId)
                .put("application_id", applicationId)
                .put("id", id())
                .put("type", type().getCode())
                .put("guild_id", guildId())
                .put("position", position())
                .put("permission_overwrites", permissionOverwrites())
                .put("name", name())
                .put("nsfw", nsfw())
                .put("parent_id", parentId())
                .put("permissions", permissions());
    }

    @NonNull
    public static GroupDM decompile(JSONObject obj, DiscordJv discordJv) {
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
        User[] recipients;
        String icon;
        String ownerId;
        String applicationId;

        try {
            ArrayList<User> usersDecompiled = new ArrayList<>();
            JSONArray usersArray = obj.getJSONArray("recipients");
            for (int i = 0; i < usersArray.length(); i++) {
                usersDecompiled.add(User.decompile(usersArray.getJSONObject(i), discordJv));
            }

            recipients = usersDecompiled.toArray(new User[0]);
        } catch (JSONException e) {
            recipients = new User[0];
        }

        try {
            icon = obj.getString("icon");
        } catch (JSONException e) {
            icon = null;
        }

        try {
            ownerId = obj.getString("owner_id");
        } catch (JSONException e) {
            ownerId = null;
        }

        try {
            applicationId = obj.getString("application_id");
        } catch (JSONException e) {
            applicationId = null;
        }

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

        return new GroupDM(id, type, guildId, position, permissionOverwrites, name, topic, nsfw, null, parentId, lastPinTimestamp, permissions, defaultThreadRateLimitPerUser, recipients, icon, ownerId, applicationId, discordJv);
    }
}
