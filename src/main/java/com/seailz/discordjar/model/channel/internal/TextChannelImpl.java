package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.TextChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

/**
 * @see TextChannel
 */
public class TextChannelImpl extends MessagingChannelImpl implements TextChannel {
    public TextChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, Category owner, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJar discordJar, JSONObject raw) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, owner, slowMode, topic, lastMessageId, defaultAutoArchiveDuration, discordJar, raw);
    }
}
