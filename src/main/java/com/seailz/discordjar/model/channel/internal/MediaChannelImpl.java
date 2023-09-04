package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.MediaChannel;
import com.seailz.discordjar.model.channel.forum.DefaultSortOrder;
import com.seailz.discordjar.model.channel.forum.ForumTag;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

public class MediaChannelImpl extends ForumChannelImpl implements MediaChannel {
    public MediaChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String postGuidelines, List<ForumTag> tags, DefaultSortOrder defaultSortOrder, String lastThreadId, JSONObject raw, DiscordJar discordJar, DefaultForumLayout defaultForumLayout) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, postGuidelines, tags, defaultSortOrder, lastThreadId, raw, discordJar, defaultForumLayout);
    }
}
