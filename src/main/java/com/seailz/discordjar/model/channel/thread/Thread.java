package com.seailz.discordjar.model.channel.thread;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.message.MessageCreateAction;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.interfaces.MessageRetrievable;
import com.seailz.discordjar.model.channel.interfaces.Messageable;
import com.seailz.discordjar.model.channel.interfaces.Transcriptable;
import com.seailz.discordjar.model.channel.interfaces.Typeable;
import com.seailz.discordjar.model.channel.internal.ThreadImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.embed.Embeder;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.message.Attachment;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a Thread object.
 * <p>
 * Threads can be either archived or active. Archived threads are generally immutable.
 * <br>To send a message or add a reaction, a thread must first be unarchived.
 * <br>The API will helpfully automatically unarchive a thread when sending a message in that thread.
 *
 * Unlike with channels, the API will only sync updates to users about threads the current user can view.
 * <br>When receiving a guild create payload, the API will only include active threads the current user can view.
 * <p>
 * Threads inside private channels are completely private to the members of that private channel.
 * <br>As such, when gaining access to a channel the API sends a thread list sync, which includes all active threads in that channel.
 *
 * Threads also track membership. Users must be added to a thread before sending messages in them.
 * <br>The API will helpfully automatically add users to a thread when sending a message in that thread.
 *
 * Guilds have limits on the number of active threads and members per thread.
 * <br>Once these are reached additional threads cannot be created or unarchived, and users cannot be added.
 * <br>Threads do not count against the per-guild channel limit.
 *
 * @author Seailz
 * @since  1.0
 * @see    GuildChannel
 */
public interface Thread extends GuildChannel, Typeable, Messageable, MessageRetrievable, Transcriptable {

    /**
     * The id of the parent channel
     */
    MessagingChannel owner();

    /**
     * Message sending rate limit in seconds.
     */
    int rateLimitPerUser();

    /**
     * ID of the {@link com.seailz.discordjar.model.user.User User} who created the thread.
     */
    String creatorId();

    /**
     * The timestamp of when the last message was pinned.
     * Null if no messages have been pinned.
     */
    String lastPinTimestamp();

    /**
     * Number of messages (not including the initial message or deleted messages) in a thread.
     */
    int messageCount();

    /**
     * Metadata about the thread.
     * @return {@link ThreadMetadata}
     */
    ThreadMetadata metadata();

    /**
     * {@link ThreadMember} object for the current user, if they have joined the thread.
     */
    ThreadMember member();

    /**
     * Similar to {@link #messageCount()} however this returns the total number of messages including deleted messages sent.
     */
    int totalMessageSent();

    /**
     * Returns the thread creation ratelimit of the parent channel when the thread was created.
     */
    int defaultThreadRateLimitPerUser();

    /**
     * ID of the last message sent in the thread.
     */
    String lastMessageId();

    @Override
    default SJSONObject compile() {
        SJSONObject obj = new SJSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("position", position());
        obj.put("nsfw", nsfw());
        obj.put("parent_id", owner().id());
        obj.put("rate_limit_per_user", rateLimitPerUser());
        obj.put("last_pin_timestamp", lastPinTimestamp());
        obj.put("last_message_id", lastMessageId());
        obj.put("thread_metadata", metadata().compile());
        obj.put("member", member().compile());
        obj.put("creator_id", creatorId());


        if (permissionOverwrites() != null) {
            SJSONArray array = new SJSONArray();
            for (PermissionOverwrite overwrite : permissionOverwrites())
                array.put(overwrite.compile());
        }

        obj.put("permission_overwrites", permissionOverwrites());
        return obj;
    }

    /**
     * Decompile a {@link SJSONObject} into a {@link GuildChannel}
     *
     * @param obj The {@link SJSONObject} to decompile
     * @param discordJar The {@link DiscordJar} instance
     *
     * @return The {@link GuildChannel} instance
     */
    @NotNull
    @Contract("_, _ -> new")
    static Thread decompile(@NotNull SJSONObject obj, @NotNull DiscordJar discordJar) {
        String id = obj.getString("id");
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        String name = obj.getString("name");
        Guild guild = obj.has("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        int position = obj.has("position") && !obj.isNull("position") ? obj.getInt("position") : 0;
        boolean nsfw = obj.has("nsfw") && !obj.isNull("nsfw") && obj.getBoolean("nsfw");
        MessagingChannel owner = obj.has("parent_id") && !obj.isNull("parent_id") ? discordJar.getTextChannelById(obj.getString("parent_id")) : null;
        int rateLimitPerUser = obj.has("rate_limit_per_user") && !obj.isNull("rate_limit_per_user") ? obj.getInt("rate_limit_per_user") : 0;
        String creatorId = obj.has("creator_id")  && !obj.isNull("creator_id") ? obj.getString("creator_id") : null;
        String lastPinTimestamp = obj.has("last_pin_timestamp") && !obj.isNull("last_pin_timestamp") ? obj.getString("last_pin_timestamp") : null;
        int messageCount = obj.has("message_count")  && !obj.isNull("message_count") ? obj.getInt("message_count") : 0;
        ThreadMetadata metadata = obj.has("thread_metadata") && !obj.isNull("thread_metadata") ? ThreadMetadata.decompile(obj.getJSONObject("thread_metadata")) : null;
        ThreadMember member = obj.has("member") && !obj.isNull("member") ? ThreadMember.decompile(obj.getJSONObject("member")) : null;
        int totalMessageSent = obj.has("total_message_sent") && !obj.isNull("total_message_sent") ? obj.getInt("total_message_sent") : 0;
        int defaultThreadRateLimitPerUser = obj.has("default_thread_rate_limit_per_user") && !obj.isNull("default_thread_rate_limit_per_user") ? obj.getInt("default_thread_rate_limit_per_user") : 0;
        String lastMessageId = obj.has("last_message_id") && !obj.isNull("last_message_id") ? obj.getString("last_message_id") : null;

        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (obj.has("permission_overwrites")) {
            SJSONArray array = obj.getJSONArray("permission_overwrites");
            for (int i = 0; i < array.length(); i++) {
                SJSONObject overwrite = array.getJSONObject(i);
                permissionOverwrites.add(PermissionOverwrite.decompile(overwrite));
            }
        }

        return new ThreadImpl(id, type, name, guild, position, permissionOverwrites, nsfw, owner, rateLimitPerUser, creatorId, lastPinTimestamp, messageCount, metadata,
                member, totalMessageSent, defaultThreadRateLimitPerUser, lastMessageId, obj, discordJar);
    }

    default MessageCreateAction sendMessage(String text) {
        return new MessageCreateAction(text, id(), discordJv());
    }

    default MessageCreateAction sendComponents(DisplayComponent... components) {
        return new MessageCreateAction(new ArrayList<>(List.of(components)), id(), discordJv());
    }

    default MessageCreateAction sendEmbeds(Embeder... embeds) {
        return new MessageCreateAction(new ArrayList<>(List.of(embeds)), id(), discordJv());
    }

    default MessageCreateAction sendAttachments(Attachment... attachments) {
        return new MessageCreateAction(new LinkedList<>(List.of(attachments)), id(), discordJv());
    }

    default void removeMember(String userId) {
        try {
            new DiscordRequest(
                    new SJSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.CHANNEL.THREAD_MEMBERS.REMOVE_THREAD_MEMBER
                            .replace("{channel.id}", id())
                            .replace("{user.id}", userId),
                    discordJv(),
                    URLS.DELETE.CHANNEL.THREAD_MEMBERS.REMOVE_THREAD_MEMBER,
                    RequestMethod.DELETE
            ).invoke();
        } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
            throw new DiscordRequest.DiscordAPIErrorException(e);
        }
    }

    default void removeMember(User user) {
        removeMember(user.id());
    }

    default void removeMember(Member member) {
        removeMember(member.user().id());
    }

    public enum AutoArchiveDuration {
        MINUTES_60(60),
        MINUTES_1440(1440),
        MINUTES_4320(4320),
        MINUTES_10080(10080);

        private final int minutes;

        AutoArchiveDuration(int minutes) {
            this.minutes = minutes;
        }

        public int minutes() {
            return minutes;
        }

        public static AutoArchiveDuration fromMinutes(int minutes) {
            for (AutoArchiveDuration duration : values()) {
                if (duration.minutes() == minutes)
                    return duration;
            }
            return null;
        }
    }

}
