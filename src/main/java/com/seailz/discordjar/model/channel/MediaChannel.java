package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjar.model.channel.forum.ForumTag;
import com.seailz.discordjar.model.channel.internal.MediaChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a media channel.
 * These are very similar to {@link ForumChannel}s, with very few differences.
 *
 * @see ForumChannel
 */
public interface MediaChannel extends ForumChannel {
    static MediaChannel decompile(JSONObject obj, DiscordJar discordJar) {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");

        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        for (Object overwrite : obj.getJSONArray("permission_overwrites")) {
            permissionOverwrites.add(PermissionOverwrite.decompile((JSONObject) overwrite));
        }

        boolean nsfw = obj.getBoolean("nsfw");
        String postGuidelines = obj.has("topic") && !obj.isNull("topic") ? obj.getString("topic") : null;
        String lastThreadId = obj.has("last_thread_id") && !obj.isNull("last_thread_id") ? obj.getString("last_thread_id") : null;
        DefaultSortOrder defaultSortOrder = obj.has("default_sort_order") && !obj.isNull("default_sort_order") ? DefaultSortOrder.fromCode(obj.getInt("default_sort_order")) : DefaultSortOrder.UNKNOWN;

        List<ForumTag> tags = new ArrayList<>();
        if (obj.has("available_tags") && !obj.isNull("available_tags")) {
            for (Object tag : obj.getJSONArray("available_tags")) {
                tags.add(ForumTag.decompile((JSONObject) tag));
            }
        }

        Guild guild = obj.has("guild_id") && !obj.isNull("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        DefaultForumLayout dfl = obj.has("default_forum_layout") && !obj.isNull("default_forum_layout") ? DefaultForumLayout.fromCode(obj.getInt("default_forum_layout")) : DefaultForumLayout.UNKNOWN;
        return new MediaChannelImpl(id, ChannelType.GUILD_FORUM, name, guild, position, permissionOverwrites, nsfw, postGuidelines, tags, defaultSortOrder, lastThreadId, obj, discordJar, dfl);
    }

    @Override
    default @NotNull ChannelType type() {
        return ChannelType.GUILD_MEDIA;
    }
}
