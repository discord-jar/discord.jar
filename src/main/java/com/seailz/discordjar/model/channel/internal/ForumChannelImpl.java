package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.ForumChannel;
import com.seailz.discordjar.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjar.model.channel.forum.ForumTag;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.util.List;

public class ForumChannelImpl extends GuildChannelImpl implements ForumChannel {

    private final String postGuidelines;
    private final List<ForumTag> tags;
    private final DefaultSortOrder defaultSortOrder;
    private final String lastThreadId;
    private final DefaultForumLayout defaultForumLayout;

    public ForumChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String postGuidelines, List<ForumTag> tags, DefaultSortOrder defaultSortOrder, String lastThreadId, SJSONObject raw, DiscordJar discordJar, DefaultForumLayout defaultForumLayout) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.postGuidelines = postGuidelines;
        this.tags = tags;
        this.defaultSortOrder = defaultSortOrder;
        this.lastThreadId = lastThreadId;
        this.defaultForumLayout = defaultForumLayout;
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

    @Override
    public DefaultForumLayout defaultForumLayout() {
        return defaultForumLayout;
    }
}
