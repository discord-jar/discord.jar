package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.TextChannel;
import com.seailz.discordjv.model.channel.thread.Thread;
import com.seailz.discordjv.model.channel.thread.ThreadMember;
import com.seailz.discordjv.model.channel.thread.ThreadMetadata;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

public class ThreadImpl extends GuildChannelImpl implements Thread {

    private final TextChannel owner;
    private final int rateLimitPerUser;
    private final String creatorId;
    private final String lastPinTimestamp;
    private final int messageCount;
    private final ThreadMetadata threadMetadata;
    private final ThreadMember threadMember;
    private final int totalMessageSent;
    private final int defaultThreadRateLimitPerUser;
    private final String lastMessageId;

    public ThreadImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, TextChannel owner, int rateLimitPerUser, String creatorId, String lastPinTimestamp, int messageCount, ThreadMetadata threadMetadata, ThreadMember threadMember, int totalMessageSent, int defaultThreadRateLimitPerUser, String lastMessageId, JSONObject raw, DiscordJv discordJv) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJv);
        this.owner = owner;
        this.rateLimitPerUser = rateLimitPerUser;
        this.creatorId = creatorId;
        this.lastPinTimestamp = lastPinTimestamp;
        this.messageCount = messageCount;
        this.threadMetadata = threadMetadata;
        this.threadMember = threadMember;
        this.totalMessageSent = totalMessageSent;
        this.defaultThreadRateLimitPerUser = defaultThreadRateLimitPerUser;
        this.lastMessageId = lastMessageId;
    }

    @Override
    public TextChannel owner() {
        return owner;
    }

    @Override
    public int rateLimitPerUser() {
        return rateLimitPerUser;
    }

    @Override
    public String creatorId() {
        return creatorId;
    }

    @Override
    public String lastPinTimestamp() {
        return lastPinTimestamp;
    }

    @Override
    public int messageCount() {
        return messageCount;
    }

    @Override
    public ThreadMetadata metadata() {
        return threadMetadata;
    }

    @Override
    public ThreadMember member() {
        return threadMember;
    }

    @Override
    public int totalMessageSent() {
        return totalMessageSent;
    }

    @Override
    public int defaultThreadRateLimitPerUser() {
        return defaultThreadRateLimitPerUser;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }
}
