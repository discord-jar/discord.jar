package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.CategoryMember;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

/**
 * Represents a category channel
 *
 * @author Seailz
 * @see Category
 * @since 1.0
 */
public class CategoryImpl extends GuildChannelImpl implements Category {
    private final List<CategoryMember> members;

    public CategoryImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, List<CategoryMember> members, JSONObject raw, DiscordJar discordJar) {
        super(id, type, name, guild, position, permissionOverwrites, false, raw, discordJar);
        this.members = members;
    }

    @Override
    public List<CategoryMember> members() {
        return this.members;
    }
}
