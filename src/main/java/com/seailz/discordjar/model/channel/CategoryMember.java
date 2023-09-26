package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.internal.CategoryMemberImpl;
import org.json.JSONObject;

/**
 * Represents a member of a {@link Category}.
 * Categories are used to group channels together, and can be used to organize channels in a server.
 * <p>
 * A category can't be a member of another category.
 *
 * @author Seailz
 * @see Category
 * @since 1.0
 */
public interface CategoryMember {
    static CategoryMember decompile(JSONObject obj, DiscordJar discordJar) {
        Category owner = (Category) discordJar.getChannelById(obj.getString("parent_id"));
        return new CategoryMemberImpl(owner, obj.getString("parent_id"));
    }

    Category owner();

    String parentId();
}
