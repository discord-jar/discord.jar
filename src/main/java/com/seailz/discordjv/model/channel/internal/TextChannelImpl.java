package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Category;
import com.seailz.discordjv.model.channel.TextChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONObject;

import java.util.List;

/**
 * @see TextChannel
 */
public class TextChannelImpl extends MessagingChannelImpl implements TextChannel {
    public TextChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, Category owner, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJv discordJv, JSONObject raw) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, owner, slowMode, topic, lastMessageId, defaultAutoArchiveDuration, discordJv, raw);
    }
}
