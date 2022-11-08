package com.seailz.javadiscordwrapper.model.channel.thread;

import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.channel.Channel;
import com.seailz.javadiscordwrapper.model.channel.utils.ChannelFlags;
import com.seailz.javadiscordwrapper.model.channel.utils.ChannelType;
import com.seailz.javadiscordwrapper.model.permission.PermissionOverwrite;
import org.json.JSONObject;

/**
 * Represents a thread
 * @author Seailz
 * @since 1.0
 */
public class Thread extends Channel implements Compilerable {

    private String ownerId;
    private int messageCount;
    private int memberCount;
    private ThreadMetadata metadata;
    private ThreadMember member;
    private int defaultAutoArchiveDuration;
    private ChannelFlags[] flags;
    private int totalMessageSent;

    public Thread(String id, ChannelType type, String guildId, int position, PermissionOverwrite[] permissionOverwrites, String name, String topic, boolean nsfw, String lastMessageId, String parentId, String lastPinTimestamp, String permissions, int defaultThreadRateLimitPerUser, String ownerId, int messageCount, int memberCount, ThreadMetadata metadata, ThreadMember member, int defaultAutoArchiveDuration, ChannelFlags[] flags, int totalMessageSent) {
        super(id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, permissions);
        this.ownerId = ownerId;
        this.messageCount = messageCount;
        this.memberCount = memberCount;
        this.metadata = metadata;
        this.member = member;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.flags = flags;
        this.totalMessageSent = totalMessageSent;
    }

    public String ownerId() {
        return ownerId;
    }

    public int messageCount() {
        return messageCount;
    }

    public int memberCount() {
        return memberCount;
    }

    public ThreadMetadata metadata() {
        return metadata;
    }

    public ThreadMember member() {
        return member;
    }

    public int defaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    public ChannelFlags[] flags() {
        return flags;
    }

    public int totalMessageSent() {
        return totalMessageSent;
    }


    @Override
    public JSONObject compile() {
        int flagTotal = 0;
        for (ChannelFlags flag : flags) {
            flagTotal += flag.getLeftShiftId();
        }
        return new JSONObject()
                .put("owner_id", ownerId)
                .put("message_count", messageCount)
                .put("member_count", memberCount)
                .put("metadata", metadata.compile())
                .put("member", member.compile())
                .put("default_auto_archive_duration", defaultAutoArchiveDuration)
                .put("flags", flags)
                .put("total_message_sent", totalMessageSent)
                .put("id", id())
                .put("type", type().getCode())
                .put("guild_id", guildId())
                .put("position", position())
                .put("permission_overwrites", permissionOverwrites())
                .put("name", name())
                .put("nsfw", nsfw())
                .put("parent_id", parentId())
                .put("permissions", permissions());
    }

    public static Thread decompile(JSONObject json) {
        String id;
        ChannelType type;
        String guildId;
        int position;
        PermissionOverwrite[] permissionOverwrites;
        String name;
        String topic;
        boolean nsfw;
        String lastMessageId;
        String parentId;
        String lastPinTimestamp;
        String permissions;
        int defaultThreadRateLimitPerUser;
        String ownerId;
        int messageCount;
        int memberCount;
        ThreadMetadata metadata;
        ThreadMember member;
        int defaultAutoArchiveDuration;
        ChannelFlags[] flags;
        int totalMessageSent;

        try {
            id = json.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            type = ChannelType.fromCode(json.getInt("type"));
        } catch (Exception e) {
            type = null;
        }

        try {
            guildId = json.getString("guild_id");
        } catch (Exception e) {
            guildId = null;
        }

        try {
            position = json.getInt("position");
        } catch (Exception e) {
            position = 0;
        }

        try {
            permissionOverwrites = new PermissionOverwrite[json.getJSONArray("permission_overwrites").length()];
            for (int i = 0; i < json.getJSONArray("permission_overwrites").length(); i++) {
                permissionOverwrites[i] = PermissionOverwrite.decompile(json.getJSONArray("permission_overwrites").getJSONObject(i));
            }
        } catch (Exception e) {
            permissionOverwrites = null;
        }

        try {
            name = json.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            topic = json.getString("topic");
        } catch (Exception e) {
            topic = null;
        }

        try {
            nsfw = json.getBoolean("nsfw");
        } catch (Exception e) {
            nsfw = false;
        }

        try {
            lastMessageId = json.getString("last_message_id");
        } catch (Exception e) {
            lastMessageId = null;
        }

        try {
            parentId = json.getString("parent_id");
        } catch (Exception e) {
            parentId = null;
        }

        try {
            lastPinTimestamp = json.getString("last_pin_timestamp");
        } catch (Exception e) {
            lastPinTimestamp = null;
        }

        try {
            permissions = json.getString("permissions");
        } catch (Exception e) {
            permissions = null;
        }

        try {
            defaultThreadRateLimitPerUser = json.getInt("default_thread_rate_limit_per_user");
        } catch (Exception e) {
            defaultThreadRateLimitPerUser = 0;
        }

        try {
            ownerId = json.getString("owner_id");
        } catch (Exception e) {
            ownerId = null;
        }

        try {
            messageCount = json.getInt("message_count");
        } catch (Exception e) {
            messageCount = 0;
        }

        try {
            memberCount = json.getInt("member_count");
        } catch (Exception e) {
            memberCount = 0;
        }

        try {
            metadata = ThreadMetadata.decompile(json.getJSONObject("metadata"));
        } catch (Exception e) {
            metadata = null;
        }

        try {
            member = ThreadMember.decompile(json.getJSONObject("member"));
        } catch (Exception e) {
            member = null;
        }

        try {
            defaultAutoArchiveDuration = json.getInt("default_auto_archive_duration");
        } catch (Exception e) {
            defaultAutoArchiveDuration = 0;
        }

        // flags
        try {
            flags = ChannelFlags.getChannelFlagsByInt(json.getInt("flags")).toArray(new ChannelFlags[0]);
        } catch (Exception e) {
            flags = null;
        }

        try {
            totalMessageSent = json.getInt("total_message_sent");
        } catch (Exception e) {
            totalMessageSent = 0;
        }

        return new Thread(id, type, guildId, position, permissionOverwrites, name, topic, nsfw, lastMessageId, parentId, lastPinTimestamp, permissions, defaultThreadRateLimitPerUser, ownerId, messageCount, memberCount, metadata, member, defaultAutoArchiveDuration, flags, totalMessageSent);
    }
}
