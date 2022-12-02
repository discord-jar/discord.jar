package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.internal.CategoryImpl;
import com.seailz.discordjv.model.channel.internal.CategoryMemberImpl;
import org.json.JSONObject;

/**
 * Represents a member of a {@link Category}.
 * Categories are used to group channels together, and can be used to organize channels in a server.
 *
 * A category can't be a member of another category.
 *
 * @author Seailz
 * @see    Category
 * @since  1.0
 */
public interface CategoryMember {
    Category owner();
    static CategoryMember decompile(JSONObject obj, DiscordJv discordJv) {
        Category owner = (Category) discordJv.getChannelById(obj.getString("parent_id"));
        return new CategoryMemberImpl(owner);
    }
}
