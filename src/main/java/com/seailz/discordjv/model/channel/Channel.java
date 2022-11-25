package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.MessageCreateAction;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import com.seailz.discordjv.model.resolve.Resolvable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class Channel implements Compilerable, Resolvable {

    private final String id;
    private final ChannelType type;
    private final String guildId;
    private final int position;
    private final PermissionOverwrite[] permissionOverwrites;
    private final String name;
    private final boolean nsfw;
    private final String parentId;
    private final String permissions;
    private final DiscordJv discordJv;

    public Channel(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, boolean nsfw, String parentId, String permissions, DiscordJv discordJv) {
        this.id = id;
        this.type = type;
        this.guildId = guildId;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.name = name;
        this.nsfw = nsfw;
        this.parentId = parentId;
        this.permissions = permissions;
        this.discordJv = discordJv;
    }

    @NonNull
    public static Channel decompile(JSONObject json, DiscordJv discordJv) {
        String id;
        ChannelType type;
        String guildId;
        int position;
        PermissionOverwrite[] permissionOverwrites;
        String name;
        boolean nsfw;
        String parentId;
        String permissions;

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
            nsfw = json.getBoolean("nsfw");
        } catch (JSONException e) {
            nsfw = false;
        }

        try {
            parentId = json.getString("parent_id");
        } catch (JSONException e) {
            parentId = null;
        }

        try {
            permissions = json.getString("permissions");
        } catch (JSONException e) {
            permissions = null;
        }

        return new Channel(id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, permissions, discordJv);

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

    public boolean nsfw() {
        return nsfw;
    }

    public String parentId() {
        return parentId;
    }

    public String permissions() {
        return permissions;
    }

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();

        if (id != null) {
            json.put("id", id);
        }

        if (type != null) {
            json.put("type", type.getCode());
        }

        if (guildId != null) {
            json.put("guild_id", guildId);
        }

        if (position != 0) {
            json.put("position", position);
        }

        if (permissionOverwrites != null) {
            JSONArray overwrites = new JSONArray();
            for (PermissionOverwrite overwrite : permissionOverwrites) {
                overwrites.put(overwrite.compile());
            }
            json.put("permission_overwrites", overwrites);
        }

        if (name != null) {
            json.put("name", name);
        }

        if (nsfw) {
            json.put("nsfw", nsfw);
        }

        if (parentId != null) {
            json.put("parent_id", parentId);
        }

        if (permissions != null) {
            json.put("permissions", permissions);
        }

        return json;
    }

    // TODO: text channel
    public MessageCreateAction sendMessage(String content) {
        return new MessageCreateAction(content, this.id, discordJv);
    }
}
