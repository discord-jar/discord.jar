package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.audio.VoiceRegion;
import com.seailz.discordjar.model.channel.interfaces.MessageRetrievable;
import com.seailz.discordjar.model.channel.interfaces.Messageable;
import com.seailz.discordjar.model.channel.interfaces.Typeable;
import com.seailz.discordjar.model.channel.internal.AudioChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents an audio channel.
 */
public interface AudioChannel extends GuildChannel, CategoryMember, Typeable, Messageable, MessageRetrievable {

    /**
     * Connects the current user to the voice channel.
     * @param vp The {@link VoiceProvider} to use.
     */
    void connect(VoiceProvider vp);

    /**
     * Connects the current user to the voice channel.
     * @param mute Whether the user should be muted.
     * @param deafen Whether the user should be deafened.
     * @param vp The {@link VoiceProvider} to use.
     */
    void connect(VoiceProvider vp, boolean mute, boolean deafen);

    /**
     * Disconnects the current user from the voice channel.
     */
    void disconnect();

    /**
     * Returns the ID of the last {@link Message} sent in the text section
     * <br>of the voice channel.
     */
    String lastMessageId();
    /**
     * Returns the {@link com.seailz.discordjar.model.channel.audio.VoiceRegion VoiceRegion} of the audio channel.
     */
    VoiceRegion region();
    /**
     * Returns the bitrate of the audio channel.
     */
    int bitrate();

    static AudioChannel decompile(JSONObject obj, DiscordJar discordJar) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        String id = obj.getString("id");
        String name = obj.getString("name");
        int position = obj.getInt("position");
        boolean nsfw = obj.getBoolean("nsfw");
        String lastMessageId = obj.has("last_message_id") && !obj.isNull("last_message_id") ? obj.getString("last_message_id") : null;
        AtomicReference<VoiceRegion> region = new AtomicReference<>();
        if (obj.has("region") && !obj.isNull("region")) discordJar.getVoiceRegions().stream().filter(r -> r.id().equals(obj.getString("region"))).findFirst().ifPresent(region::set);
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

        return new AudioChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, lastMessageId, region.get(), category, bitrate, obj, discordJar);
    }
}
