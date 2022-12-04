package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjv.model.channel.forum.ForumTag;
import com.seailz.discordjv.model.channel.internal.ForumChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a forum channel, or their official name, <b>Thread Only Channel</b>.
 * <br>This is a channel that is a container for a group of threads. At the moment private threads are not available in forum channels,
 * though Discord staff said they may be added in the future.
 * <p>
 * Posts (or threads) in forum channels can have {@link com.seailz.discordjv.model.channel.forum.ForumTag tags}, which are used to categorize the post. Tags are created by moderators of the server.
 * <br>Posts can also have a pinned state, which means that the post will be shown at the top of the channel.
 * <p>
 * There is also a default reaction feature, which will add a reaction (with 0 uses) to the post when it is created.
 * <br>The reaction will be shown on the main page of the channel next to the post.
 * <p>
 * Forum channels have a feature called <b>default_sort_order</b>, which allows you to sort posts by {@link com.seailz.discordjv.model.channel.forum.DefaultSortOrder DefaultSortOrder}.
 * <br>At the moment there are only two sort orders, LATEST_ACTIVITY and CREATION_DATE.
 * <p>
 * There is also currently a <a href="https://github.com/discord/discord-api-docs/pull/5693">PR</a> on the Discord API docs to add the `default_form_layout` field.
 * <br>The field will let you either show posts as a list or as a grid.
 *
 * @author Seailz
 * @since  1.0
 * @see    GuildChannel
 */
public interface ForumChannel extends GuildChannel {

    /**
     * The topic of the channel.
     * In this case, the topic is the posting guidelines.
     */
    String postGuidelines();

    /**
     * A list of {@link ForumTag forum tags} that can be applied to posts.
     * If they are moderator tags, they will have the {@link ForumTag#moderated()} field set to true, and only moderators can apply them to posts.
     */
    List<ForumTag> tags();

    /**
     * The default sort order of the channel.
     * @see com.seailz.discordjv.model.channel.forum.DefaultSortOrder
     */
    DefaultSortOrder defaultSortOrder();

    /**
     * The ID of the last thread that was created in the channel.
     */
    String lastThreadId();

    @Override
    default JSONObject compile() {
        JSONArray permissionOverwrites = new JSONArray();
        permissionOverwrites.forEach(o -> permissionOverwrites.put(((PermissionOverwrite) o).compile()));

        JSONArray tags = new JSONArray();
        tags.forEach(o -> tags.put(((ForumTag) o).compile()));
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("position", position());
        obj.put("nsfw", nsfw());
        obj.put("permission_overwrites", permissionOverwrites);
        obj.put("topic", postGuidelines());
        obj.put("last_thread_id", lastThreadId());
        obj.put("default_sort_order", defaultSortOrder().getCode());
        obj.put("available_tags", tags);
        return obj;
    }

    static ForumChannel decompile(JSONObject obj, DiscordJv discordJv) {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");
        List<PermissionOverwrite> permissionOverwrites = obj.getJSONArray("permission_overwrites").toList().stream().map(o -> PermissionOverwrite.decompile((JSONObject) o)).toList();
        boolean nsfw = obj.getBoolean("nsfw");
        String postGuidelines = obj.has("topic") ? obj.getString("topic") : null;
        String lastThreadId = obj.has("last_message_id") ? obj.getString("last_thread_id") : null;
        DefaultSortOrder defaultSortOrder = DefaultSortOrder.fromCode(obj.getInt("default_sort_order"));
        List<ForumTag> tags = obj.has("available_tags") ? obj.getJSONArray("available_tags").toList().stream().map(o -> ForumTag.decompile((JSONObject) o)).toList() : null;
        Guild guild = discordJv.getGuildById(obj.getString("guild_id"));
        return new ForumChannelImpl(id, ChannelType.GUILD_FORUM, name, guild, position, permissionOverwrites, nsfw, postGuidelines, tags, defaultSortOrder, lastThreadId);
    }
}
