package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.audio.VideoQualityMode;
import com.seailz.discordjar.model.channel.audio.VoiceRegion;
import com.seailz.discordjar.model.channel.interfaces.Messageable;
import com.seailz.discordjar.model.channel.internal.VoiceChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A voice channel.
 */
public interface VoiceChannel extends AudioChannel {

    int userLimit();
    VideoQualityMode videoQualityMode();

    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("position", position());
        obj.put("permission_overwrites", permissionOverwrites());
        obj.put("nsfw", nsfw());
        obj.put("last_message_id", lastMessageId());
        obj.put("region", region());
        obj.put("category_id", owner().id());
        obj.put("bitrate", bitrate());
        obj.put("user_limit", userLimit());
        obj.put("video_quality_mode", videoQualityMode());
        return obj;
    }

    static VoiceChannel decompile(JSONObject obj, DiscordJar discordJar) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");
        boolean nsfw = obj.getBoolean("nsfw");
        String lastMessageId = obj.getString("last_message_id");
        AtomicReference<VoiceRegion> region = new AtomicReference<>();
        discordJar.getVoiceRegions().stream().filter(r -> r.id().equals(obj.getString("region"))).findFirst().ifPresent(region::set);
        int bitrate = obj.getInt("bitrate");
        Category category = Category.fromId(obj.getString("parent_id"), discordJar);
        JSONArray array = obj.getJSONArray("permission_overwrites");
        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (array.length() > 0) {
            permissionOverwrites = new ArrayList<>();
            for (int i = 0; i < array.length(); i++)
                permissionOverwrites.add(PermissionOverwrite.decompile(array.getJSONObject(i)));
        }
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        Guild guild = obj.has("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        int userLimit = obj.getInt("user_limit");
        VideoQualityMode videoQualityMode = VideoQualityMode.fromCode(obj.getInt("video_quality_mode"));

        return new VoiceChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, lastMessageId, region.get(), category, bitrate, userLimit, videoQualityMode, obj, discordJar);
    }
}
