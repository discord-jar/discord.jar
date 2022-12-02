package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.VoiceChannel;
import com.seailz.discordjv.model.channel.audio.VideoQualityMode;
import com.seailz.discordjv.model.channel.audio.VoiceRegion;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;

import java.util.List;

/**
 * Impl of {@link VoiceChannel}.
 */
public class VoiceChannelImpl extends AudioChannelImpl implements VoiceChannel {

    private final int userLimit;
    private final VideoQualityMode videoQualityMode;

    public VoiceChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String lastMessageId, VoiceRegion region, Category category, int bitrate, int userLimit, VideoQualityMode videoQualityMode) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, lastMessageId, region, category, bitrate);
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
