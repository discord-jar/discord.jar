package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.MessagingChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;

import java.util.List;

public class MessagingChannelImpl extends GuildChannelImpl implements MessagingChannel {

    private final Category category;
    private final int slowMode;
    private final String topic;
    private final String lastMessageId;
    private final int defaultAutoArchiveDuration;
    private final DiscordJv discordJv;


    public MessagingChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, Category owner, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJv discordJv) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw);
        this.category = owner;
        this.slowMode = slowMode;
        this.topic = topic;
        this.lastMessageId = lastMessageId;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.discordJv = discordJv;
    }

    @Override
    public Category owner() {
        return null;
    }

    @Override
    public int slowMode() {
        return slowMode;
    }

    @Override
    public String topic() {
        return topic;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }

    @Override
    public int defaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    @Override
    public DiscordJv discordJv() {
        return discordJv;
    }
}