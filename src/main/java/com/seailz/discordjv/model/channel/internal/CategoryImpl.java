package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.CategoryMember;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;

import java.util.List;

/**
 * Represents a category channel
 *
 * @author Seailz
 * @since  1.0
 * @see    Category
 */
public class CategoryImpl extends GuildChannelImpl implements Category  {
    private final List<CategoryMember> members;

    public CategoryImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, List<CategoryMember> members) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw);
        this.members = members;
    }

    @Override
    public List<CategoryMember> members() {
        return this.members;
    }
}
