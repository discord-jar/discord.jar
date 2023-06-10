package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.message.StartThreadForumChannelAction;
import com.seailz.discordjar.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjar.model.channel.forum.ForumTag;
import com.seailz.discordjar.model.channel.interfaces.Typeable;
import com.seailz.discordjar.model.channel.internal.ForumChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a forum channel, or their official name, <b>Thread Only Channel</b>.
 * <br>This is a channel that is a container for a group of threads. At the moment private threads are not available in forum channels,
 * though Discord staff said they may be added in the future.
 * <p>
 * Posts (or threads) in forum channels can have {@link com.seailz.discordjar.model.channel.forum.ForumTag tags}, which are used to categorize the post. Tags are created by moderators of the server.
 * <br>Posts can also have a pinned state, which means that the post will be shown at the top of the channel.
 * <p>
 * There is also a default reaction feature, which will add a reaction (with 0 uses) to the post when it is created.
 * <br>The reaction will be shown on the main page of the channel next to the post.
 * <p>
 * Forum channels have a feature called <b>default_sort_order</b>, which allows you to sort posts by {@link com.seailz.discordjar.model.channel.forum.DefaultSortOrder DefaultSortOrder}.
 * <br>At the moment there are only two sort orders, LATEST_ACTIVITY and CREATION_DATE.
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
     * @see com.seailz.discordjar.model.channel.forum.DefaultSortOrder
     */
    DefaultSortOrder defaultSortOrder();

    /**
     * The ID of the last thread that was created in the channel.
     */
    String lastThreadId();

    DefaultForumLayout defaultForumLayout();

    default StartThreadForumChannelAction startThread(String name, StartThreadForumChannelAction.ForumThreadMessageParams message) {
        return new StartThreadForumChannelAction(id(), name, message, djv());
    }

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

    static ForumChannel decompile(JSONObject obj, DiscordJar discordJar) {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");

        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        for (Object overwrite : obj.getJSONArray("permission_overwrites")) {
            permissionOverwrites.add(PermissionOverwrite.decompile((JSONObject) overwrite));
        }

        boolean nsfw = obj.getBoolean("nsfw");
        String postGuidelines = obj.has("topic") && !obj.isNull("topic") ? obj.getString("topic") : null;
        String lastThreadId = obj.has("last_thread_id") && !obj.isNull("last_thread_id")  ? obj.getString("last_thread_id") : null;
        DefaultSortOrder defaultSortOrder = obj.has("default_sort_order") && !obj.isNull("default_sort_order")  ? DefaultSortOrder.fromCode(obj.getInt("default_sort_order")) : DefaultSortOrder.UNKNOWN;

        List<ForumTag> tags = new ArrayList<>();
        if (obj.has("available_tags") && !obj.isNull("available_tags")) {
            for (Object tag : obj.getJSONArray("available_tags")) {
                tags.add(ForumTag.decompile((JSONObject) tag));
            }
        }

        Guild guild = obj.has("guild_id") && !obj.isNull("guild_id") ?  discordJar.getGuildById(obj.getString("guild_id")) : null;
        DefaultForumLayout dfl = obj.has("default_forum_layout") && !obj.isNull("default_forum_layout")  ? DefaultForumLayout.fromCode(obj.getInt("default_forum_layout")) : DefaultForumLayout.UNKNOWN;
        return new ForumChannelImpl(id, ChannelType.GUILD_FORUM, name, guild, position, permissionOverwrites, nsfw, postGuidelines, tags, defaultSortOrder, lastThreadId, obj, discordJar, dfl);
    }

    /**
     * The layout in which to display posts in a forum channel.
     * Defaults to <b>NOT_SET</b> if a layout view has not been specified
     * <br>by a channel admin.
     *
     * @author Seailz
     * @see ForumChannel
     * @since 1.0
     */
    enum DefaultForumLayout {

        NOT_SET(0), // No default has been set.
        LIST_VIEW(1), // Displays posts as a list.
        GALLERY_VIEW(2), // Displays posts as a collection of tiles.

        UNKNOWN(-1);
        private final int code;
        DefaultForumLayout(int code) {
            this.code = code;
        }
        public int getCode() { return code; }
        public static DefaultForumLayout fromCode(int code) {
            for (DefaultForumLayout value : values()) {
                if (value.getCode() == code) return value;
            }
            return UNKNOWN;
        }
    }
}
