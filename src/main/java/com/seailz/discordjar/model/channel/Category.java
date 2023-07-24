package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.internal.CategoryImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface Category extends GuildChannel {

    List<CategoryMember> members();

    static Category decompile(JSONObject obj, DiscordJar discordJar) {
        String id = obj.getString("id");
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        String name = obj.getString("name");
        Guild guild = obj.has("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        int position = obj.getInt("position");

        JSONArray array = obj.getJSONArray("permission_overwrites");
        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (array.length() > 0) {
            permissionOverwrites = new ArrayList<>();
            for (int i = 0; i < array.length(); i++)
                permissionOverwrites.add(PermissionOverwrite.decompile(array.getJSONObject(i)));
        }

        List<CategoryMember> channels = new ArrayList<>();
        if (guild != null) {
            guild.getChannels().forEach(channel -> {
                if (channel instanceof CategoryMember)
                    if (((CategoryMember) channel).owner().id().equals(id))
                        channels.add((CategoryMember) channel);
            });
        }
        return new CategoryImpl(id, type, name, guild, position, permissionOverwrites, channels, obj, discordJar);
    }

    static Category fromId(String id, DiscordJar discordJar) {
        return discordJar.getCategoryById(id);
    }

}
