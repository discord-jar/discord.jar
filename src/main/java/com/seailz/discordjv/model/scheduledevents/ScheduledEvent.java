package com.seailz.discordjv.model.scheduledevents;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.VoiceChannel;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.timestamp.ISO8601;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Represents a scheduled event.
 *
 * @param id The id of the scheduled event.
 * @param guild The guild the scheduled event is in.
 * @param channel The channel the scheduled event is in.
 * @param creatorId The id of the user that created the scheduled event. (This will not be included for events created after October 25th, 2021)
 * @param name The name of the scheduled event.
 * @param description The description of the scheduled event.
 * @param scheduledStartTime The time the scheduled event will start.
 * @param scheduledEndTime The time the scheduled event will end.
 * @param privacyLevel The privacy level of the scheduled event. See {@link PrivacyLevel}
 * @param status The status of the scheduled event. See {@link EventStatus}
 * @param entityType The type of entity the scheduled event is for. See {@link EntityType}
 * @param entityId The id of the entity the scheduled event is for.
 * @param entityMetadata The metadata of the entity the scheduled event is for.
 * @param creator The user that created the scheduled event. (This will not be included for events created after October 25th, 2021)
 * @param subscriberCount The number of users subscribed to the scheduled event.
 * @param image The image of the scheduled event.
 *
 * @author Seailz
 * @since 1.0
 * @see <a href="https://discord.com/developers/docs/resources/scheduled-event#scheduled-event-object">Scheduled Event Object</a>
 */
public record ScheduledEvent(
        String id,
        Guild guild,
        VoiceChannel channel,
        String creatorId, // Will be null if the event was created before October 25th 2021.
        String name,
        String description,
        ISO8601 scheduledStartTime,
        ISO8601 scheduledEndTime, // required if entityType is EXTERNAL
        PrivacyLevel privacyLevel,
        EventStatus status,
        EntityType entityType,
        String entityId,
        EntityMetadata entityMetadata,
        User creator,
        int subscriberCount,
        String image // TODO: sort out images!!
) implements Compilerable {

    @NotNull
    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("guild_id", guild.id());
        json.put("channel_id", channel.id());
        json.put("creator_id", creatorId);
        json.put("name", name);
        json.put("description", description);
        json.put("scheduled_start_time", scheduledStartTime.toString());
        json.put("scheduled_end_time", scheduledEndTime.toString());
        json.put("privacy_level", privacyLevel.getValue());
        json.put("status", status.getValue());
        json.put("entity_type", entityType.getValue());
        json.put("entity_id", entityId);
        json.put("entity_metadata", entityMetadata.compile());
        json.put("creator", creator.compile());
        json.put("user_count", subscriberCount);
        json.put("image", image);
        return json;
    }

    @NotNull
    public static ScheduledEvent decompile(@NotNull JSONObject obj, @NotNull DiscordJv discordJv) {
        String id = obj.has("id") ? obj.getString("id") : null;
        Guild guild = obj.has("guild_id") ? discordJv.getGuildById(obj.getString("guild_id")) : null;
        VoiceChannel channel = obj.has("channel_id") ? (VoiceChannel) discordJv.getChannelById(obj.getString("channel_id")) : null;
        String creatorId = obj.has("creator_id") ? obj.getString("creator_id") : null;
        String name = obj.has("name") ? obj.getString("name") : null;
        String description = obj.has("description") ? obj.getString("description") : null;
        ISO8601 scheduledStartTime = obj.has("scheduled_start_time") ? new ISO8601(obj.getString("scheduled_start_time")) : null;
        ISO8601 scheduledEndTime = obj.has("scheduled_end_time") ? new ISO8601(obj.getString("scheduled_end_time")) : null;
        PrivacyLevel privacyLevel = obj.has("privacy_level") ? PrivacyLevel.fromValue(obj.getInt("privacy_level")) : PrivacyLevel.UNKNOWN;
        EventStatus status = obj.has("status") ? EventStatus.fromValue(obj.getInt("status")) : EventStatus.UNKNOWN;
        EntityType entityType = obj.has("entity_type") ? EntityType.fromValue(obj.getInt("entity_type")) : EntityType.UNKNOWN;
        String entityId = obj.has("entity_id") ? obj.getString("entity_id") : null;
        EntityMetadata entityMetadata = obj.has("entity_metadata") ? EntityMetadata.decompile(obj.getJSONObject("entity_metadata")) : null;
        User creator = obj.has("creator") ? User.decompile(obj.getJSONObject("creator"), discordJv) : null;
        int subscriberCount = obj.has("user_count") ? obj.getInt("user_count") : 0;
        String image = obj.has("image") ? obj.getString("image") : null;

        return new ScheduledEvent(id, guild, channel, creatorId, name, description, scheduledStartTime, scheduledEndTime, privacyLevel, status, entityType, entityId, entityMetadata, creator, subscriberCount, image);
    }

    /**
     * The privacy level of the scheduled event
     */
    enum PrivacyLevel {

        GUILD_ONLY(2), // 	the scheduled event is only accessible to guild members
        UNKNOWN(-1); // 	unknown privacy level

        private final int value;

        PrivacyLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PrivacyLevel fromValue(int value) {
            for (PrivacyLevel level : values()) {
                if (level.getValue() == value) {
                    return level;
                }
            }
            return UNKNOWN;
        }
    }
    /**
     * The status of the scheduled event
     */
    enum EventStatus {
        SCHEDULED(1), // 	the scheduled event has been created, but the scheduled start time has not passed
        ACTIVE(2),    //  the scheduled event’s scheduled start time has passed, but the scheduled end time has not passed
        COMPLETED(3), // 	the scheduled event’s scheduled end time has passed
        CANCELED(4),  // 	the scheduled event was canceled
        UNKNOWN(-1);  // 	unknown event status
        private final int value;

        EventStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EventStatus fromValue(int value) {
            for (EventStatus status : values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }
    /**
     * the type of the scheduled event
     */
    enum EntityType {
        STAGE_INSTANCE(1), // 	the scheduled event is associated with a stage instance
        VOICE(2),          // 	the scheduled event is not associated with a stage instance
        EXTERNAL(3),       // 	the scheduled event is associated with an external entity
        UNKNOWN(-1);       // 	unknown entity type

        private final int value;

        EntityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EntityType fromValue(int value) {
            for (EntityType type : values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }
    /**
     * Metadata about the scheduled event entity
     */
    interface EntityMetadata extends Compilerable {
        static @Nullable EntityMetadata decompile(@NotNull JSONObject obj) {
            if (obj.has("location")) {
                return new ExternalEntityMetadata(obj.getString("location"));
            }
            return null;
        }
    }
    /**
    * Metadata about an external event entity
    *
    * @param location The location of the scheduled event
    */
    record ExternalEntityMetadata(String location) implements EntityMetadata {
            @Override
            public JSONObject compile() {
                return new JSONObject()
                        .put("location", location);
            }
        }

}
