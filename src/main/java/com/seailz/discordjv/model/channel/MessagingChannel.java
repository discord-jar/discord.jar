package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.MessageCreateAction;
import com.seailz.discordjv.model.channel.internal.MessagingChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.component.DisplayComponent;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A channel that can be used to send messages.
 *
 * @author Seailz
 * @since  1.0
 * @see    GuildChannel
 */
public interface MessagingChannel extends GuildChannel, CategoryMember {

    /**
     * Amount of seconds a user has to wait before sending another message
     */
    int slowMode();

    /**
     * The topic of the channel
     */
    String topic();

    /**
     * The id of the last {@link com.seailz.discordjv.model.message.Message message} sent in this channel
     */
    String lastMessageId();

    /**
     * The duration in minutes until when a thread is archived
     * <br>This can be <b>60</b>, <b>1440</b>, <b>4320</b> or <b>10080</b>
     */
    int defaultAutoArchiveDuration();

    DiscordJv discordJv();

    default MessageCreateAction sendMessage(String text) {
        return new MessageCreateAction(text, id(), discordJv());
    }

    default MessageCreateAction sendComponents(DisplayComponent... components) {
        return new MessageCreateAction(new ArrayList<>(List.of(components)), id(), discordJv());
    }

    static MessagingChannel decompile(JSONObject obj, DiscordJv discordJv) {
        String id = obj.getString("id");
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        String name = obj.getString("name");
        Guild guild = obj.has("guild_id") ? discordJv.getGuildById(obj.getString("guild_id")) : null;
        boolean nsfw = obj.has("nsfw") && obj.getBoolean("nsfw");
        int position = obj.getInt("position");
        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (obj.has("permission_overwrites")) {
            JSONArray array = obj.getJSONArray("permission_overwrites");
            for (int i = 0; i < array.length(); i++)
                permissionOverwrites.add(PermissionOverwrite.decompile(array.getJSONObject(i)));
        }

        int slowMode = obj.has("rate_limit_per_user") ? obj.getInt("rate_limit_per_user") : 0;
        System.out.println(obj.getJSONObject("topic").toString());
        String topic = obj.has("topic") ? obj.getString("topic") : null;
        String lastMessageId = obj.has("last_message_id") ? obj.getString("last_message_id") : null;
        int defaultAutoArchiveDuration = obj.has("default_auto_archive_duration") ? obj.getInt("default_auto_archive_duration") : 0;
        return new MessagingChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, Category.fromId("parent_id", discordJv) ,slowMode, topic, lastMessageId, defaultAutoArchiveDuration, discordJv);
    }

    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("position", position());
        obj.put("nsfw", nsfw());
        obj.put("rate_limit_per_user", slowMode());
        obj.put("topic", topic());
        obj.put("last_message_id", lastMessageId());
        obj.put("default_auto_archive_duration", defaultAutoArchiveDuration());

        if (permissionOverwrites() != null) {
            JSONArray array = new JSONArray();
            for (PermissionOverwrite overwrite : permissionOverwrites())
                array.put(overwrite.compile());
        }

        obj.put("permission_overwrites", permissionOverwrites());
        return obj;
    }
}
