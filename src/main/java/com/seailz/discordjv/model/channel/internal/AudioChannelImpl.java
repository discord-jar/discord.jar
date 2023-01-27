package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.AudioChannel;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.audio.VoiceRegion;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

public class AudioChannelImpl extends GuildChannelImpl implements AudioChannel {

    private final String lastMessageId;
    private final VoiceRegion region;
    private final int bitrate;
    private final Category category;

    public AudioChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String lastMessageId, VoiceRegion region, Category category, int bitrate, JSONObject raw, DiscordJv discordJv) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJv);
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
