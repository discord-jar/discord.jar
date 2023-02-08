package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

public class MessagingChannelImpl extends GuildChannelImpl implements MessagingChannel {

    private final Category category;
    private final int slowMode;
    private final String topic;
    private final String lastMessageId;
    private final int defaultAutoArchiveDuration;
    private final DiscordJar discordJar;


    public MessagingChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, Category owner, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJar discordJar, JSONObject raw) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.category = owner;
        this.slowMode = slowMode;
        this.topic = topic;
        this.lastMessageId = lastMessageId;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.discordJar = discordJar;
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
    public @NotNull DiscordJar discordJv() {
        return discordJar;
    }
}