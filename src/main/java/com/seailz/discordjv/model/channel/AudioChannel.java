package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.audio.VoiceRegion;
import com.seailz.discordjv.model.channel.internal.AudioChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.message.Message;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents an audio channel.
 */
public interface AudioChannel extends GuildChannel, CategoryMember {
    /**
     * Returns the ID of the last {@link Message} sent in the text section
     * <br>of the voice channel.
     */
    String lastMessageId();
    /**
     * Returns the {@link com.seailz.discordjv.model.channel.audio.VoiceRegion VoiceRegion} of the audio channel.
     */
    VoiceRegion region();
    /**
     * Returns the bitrate of the audio channel.
     */
    int bitrate();

    static AudioChannel decompile(JSONObject obj, DiscordJv discordJv) {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");
        boolean nsfw = obj.getBoolean("nsfw");
        String lastMessageId = obj.getString("last_message_id");
        AtomicReference<VoiceRegion> region = new AtomicReference<>();
        discordJv.getVoiceRegions().stream().filter(r -> r.id().equals(obj.getString("region"))).findFirst().ifPresent(region::set);
        int bitrate = obj.getInt("bitrate");
        Category category = Category.fromId(obj.getString("parent_id"), discordJv);
        JSONArray array = obj.getJSONArray("permission_overwrites");
        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (array.length() > 0) {
            permissionOverwrites = new ArrayList<>();
            for (int i = 0; i < array.length(); i++)
                permissionOverwrites.add(PermissionOverwrite.decompile(array.getJSONObject(i)));
        }
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        Guild guild = obj.has("guild_id") ? discordJv.getGuildById(obj.getString("guild_id")) : null;

        return new AudioChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, lastMessageId, region.get(), category, bitrate);
    }
}
