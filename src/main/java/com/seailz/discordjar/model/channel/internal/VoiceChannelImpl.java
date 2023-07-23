package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.VoiceChannel;
import com.seailz.discordjar.model.channel.audio.VideoQualityMode;
import com.seailz.discordjar.model.channel.audio.VoiceRegion;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.util.List;

/**
 * Impl of {@link VoiceChannel}.
 */
public class VoiceChannelImpl extends AudioChannelImpl implements VoiceChannel {

    private final int userLimit;
    private final VideoQualityMode videoQualityMode;

    public VoiceChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String lastMessageId, VoiceRegion region, Category category, int bitrate, int userLimit, VideoQualityMode videoQualityMode, SJSONObject raw, DiscordJar discordJar) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, lastMessageId, region, category, bitrate, raw, discordJar);
        this.userLimit = userLimit;
        this.videoQualityMode = videoQualityMode;
    }

    @Override
    public int userLimit() {
        return userLimit;
    }

    @Override
    public VideoQualityMode videoQualityMode() {
        return videoQualityMode;
    }
}
