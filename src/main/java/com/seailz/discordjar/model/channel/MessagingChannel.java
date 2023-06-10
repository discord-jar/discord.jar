package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.message.MessageCreateAction;
import com.seailz.discordjar.model.channel.interfaces.MessageRetrievable;
import com.seailz.discordjar.model.channel.interfaces.Messageable;
import com.seailz.discordjar.model.channel.interfaces.Transcriptable;
import com.seailz.discordjar.model.channel.interfaces.Typeable;
import com.seailz.discordjar.model.channel.internal.MessagingChannelImpl;
import com.seailz.discordjar.model.channel.transcript.TranscriptFormatter;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embed;
import com.seailz.discordjar.model.embed.EmbedField;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A channel that can be used to send messages.
 *
 * @author Seailz
 * @since  1.0
 * @see    GuildChannel
 */
public interface MessagingChannel extends GuildChannel, CategoryMember, Typeable, Messageable, MessageRetrievable, Transcriptable {

    /**
     * Amount of seconds a user has to wait before sending another message
     */
    int slowMode();

    /**
     * The topic of the channel
     */
    String topic();

    /**
     * The id of the last {@link com.seailz.discordjar.model.message.Message message} sent in this channel
     */
    String lastMessageId();

    /**
     * Given a set of message ids, this method will bulk delete them.
     * <br><b>If there are any messages that are older than 2 weeks, this will fail.</b>
     * That's a Discord limitation, not our fault.
     *
     * @param messageIds List of message ids that you want to delete. Must be between 2 and 100 messages.
     *                   Invalid message ids will count towards this limit.
     * @param filterMessages If this is true, instead of simply throwing an error, this method will filter all the messages that are older than 2 weeks and delete the rest.
     * @param reason You can specify a reason for the audit log.
     * @return {@link Response<Void>}
     */
    Response<Void> bulkDeleteMessages(List<String> messageIds, boolean filterMessages, String reason);

    /**
     * Given a set of message ids, this method will bulk delete them.
     * <br><b>If there are any messages that are older than 2 weeks, this will filter them and won't delete them.</b>
     * That's a Discord limitation, not our fault.
     *
     * @param messageIds List of message ids that you want to delete. Must be between 2 and 100 messages.
     *                   Invalid message ids will count towards this limit.
     * @return {@link Response<Void>}
     */
    default Response<Void> bulkDeleteMessages(List<String> messageIds) {
        return bulkDeleteMessages(messageIds, true, null);
    }

    /**
     * Given a set of message ids, this method will bulk delete them.
     * <br><b>If there are any messages that are older than 2 weeks, this will filter them and won't delete them.</b>
     * That's a Discord limitation, not our fault.
     *
     * @param messageIds List of message ids that you want to delete. Must be between 2 and 100 messages.
     *                   Invalid message ids will count towards this limit.
     * @param filterMessages If this is true, instead of simply throwing an error, this method will filter all the messages that are older than 2 weeks and delete the rest.
     * @return {@link Response<Void>}
     */
    default Response<Void> bulkDeleteMessages(List<String> messageIds, boolean filterMessages) {
        return bulkDeleteMessages(messageIds, filterMessages, null);
    }

    /**
     * Given a set of message ids, this method will bulk delete them.
     * <br><b>If there are any messages that are older than 2 weeks, this will filter them and won't delete them.</b>
     * That's a Discord limitation, not our fault.
     *
     * @param messageIds List of message ids that you want to delete. Must be between 2 and 100 messages.
     *                   Invalid message ids will count towards this limit.
     * @param reason You can specify a reason for the audit log.
     * @return {@link Response<Void>}
     */
    default Response<Void> bulkDeleteMessages(List<String> messageIds, String reason) {
        return bulkDeleteMessages(messageIds, true, reason);
    }

    /**
     * The duration in minutes until when a thread is archived
     * <br>This can be <b>60</b>, <b>1440</b>, <b>4320</b> or <b>10080</b>
     */
    int defaultAutoArchiveDuration();

    @NotNull DiscordJar discordJv();

    static MessagingChannel decompile(JSONObject obj, DiscordJar discordJar) {
        String id = obj.getString("id");
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        String name = obj.getString("name");
        Guild guild = obj.has("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        boolean nsfw = obj.has("nsfw") && obj.getBoolean("nsfw");
        int position = obj.getInt("position");
        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (obj.has("permission_overwrites")) {
            JSONArray array = obj.getJSONArray("permission_overwrites");
            for (int i = 0; i < array.length(); i++)
                permissionOverwrites.add(PermissionOverwrite.decompile(array.getJSONObject(i)));
        }

        int slowMode = obj.has("rate_limit_per_user") ? obj.getInt("rate_limit_per_user") : 0;
        String topic = !obj.has("topic") || obj.get("topic") == null || obj.get("topic").equals(JSONObject.NULL) ? null : obj.getString("topic");
        String lastMessageId = !obj.has("last_message_id") || obj.get("last_message_id") == null || obj.get("last_message_id").equals(JSONObject.NULL) ? null : obj.getString("last_message_id");
        int defaultAutoArchiveDuration = obj.has("default_auto_archive_duration") ? obj.getInt("default_auto_archive_duration") : 0;
        return new MessagingChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, obj.has("parent_id") && !obj.isNull("parent_id") ? Category.fromId(obj.getString("parent_id"), discordJar) : null ,slowMode, topic, lastMessageId, defaultAutoArchiveDuration, discordJar, obj);
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
