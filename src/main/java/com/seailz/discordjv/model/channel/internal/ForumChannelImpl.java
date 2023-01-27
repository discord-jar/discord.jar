package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.ForumChannel;
import com.seailz.discordjv.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjv.model.channel.forum.ForumTag;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

public class ForumChannelImpl extends GuildChannelImpl implements ForumChannel {

    private final String postGuidelines;
    private final List<ForumTag> tags;
    private final DefaultSortOrder defaultSortOrder;
    private final String lastThreadId;

    public ForumChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String postGuidelines, List<ForumTag> tags, DefaultSortOrder defaultSortOrder, String lastThreadId, JSONObject raw, DiscordJv discordJv) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJv);
        this.postGuidelines = postGuidelines;
        this.tags = tags;
        this.defaultSortOrder = defaultSortOrder;
        this.lastThreadId = lastThreadId;
    }

    @Override
    public String postGuidelines() {
        return postGuidelines;
    }

    @Override
    public List<ForumTag> tags() {
        return tags;
    }

    @Override
    public DefaultSortOrder defaultSortOrder() {
        return defaultSortOrder;
    }

    @Override
    public String lastThreadId() {
        return lastThreadId;
    }
}
