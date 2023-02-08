package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.AudioChannel;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.audio.VoiceRegion;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

public class AudioChannelImpl extends GuildChannelImpl implements AudioChannel {

    private final String lastMessageId;
    private final VoiceRegion region;
    private final int bitrate;
    private final Category category;

    public AudioChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String lastMessageId, VoiceRegion region, Category category, int bitrate, JSONObject raw, DiscordJar discordJar) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.lastMessageId = lastMessageId;
        this.region = region;
        this.bitrate = bitrate;
        this.category = category;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }

    @Override
    public VoiceRegion region() {
        return region;
    }

    @Override
    public int bitrate() {
        return bitrate;
    }

    @Override
    public Category owner() {
        return category;
    }
}
