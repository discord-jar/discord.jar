package com.seailz.discordjar.model.guild.scheduledevents;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.CDNAble;
import com.seailz.discordjar.utils.StringFormatter;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.joda.time.DateTime;
import org.json.JSONObject;

public record ScheduledEvent(
        String id,
        String guildId,
        String channelId,
        String creatorId,
        String name,
        String description,
        DateTime scheduledStart,
        DateTime scheduledEnd,
        PrivacyLevel privacyLevel,
        EventStatus eventStatus,
        EntityType entityType,
        String entityId,
        EntityMetadata entityMetadata,
        User creator,
        int userCount,
        String coverHash
) implements CDNAble, Compilerable {


    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("guild_id", this.guildId);
        obj.put("channel_id", this.channelId);
        obj.put("creator_id", this.creatorId);
        obj.put("name", this.name);
        obj.put("description", this.description);
        obj.put("scheduled_start_time", this.scheduledStart.toString());
        obj.put("scheduled_end_time", this.scheduledEnd.toString());
        obj.put("privacy_level", this.privacyLevel.getValue());
        obj.put("status", this.eventStatus.getValue());
        obj.put("entity_type", this.entityType.getValue());
        obj.put("entity_id", this.entityId);
        obj.put("entity_metadata", this.entityMetadata.compile());
        obj.put("creator", this.creator.compile());
        obj.put("user_count", this.userCount);
        obj.put("cover_image_hash", this.coverHash);
        return obj;
    }

    public static ScheduledEvent decompile(JSONObject obj, DiscordJar jar) {
        String id = obj.getString("id");
        String guildId = obj.getString("guild_id");
        String channelId = obj.has("channel_id") && !obj.isNull("channel_id") ? obj.getString("channel_id") : null;
        String creatorId = obj.has("creator_id") && !obj.isNull("creator_id") ? obj.getString("creator_id") : null;
        String name = obj.has("name") && !obj.isNull("name") ? obj.getString("name") : null;
        String description = obj.has("description") && !obj.isNull("description") ? obj.getString("description") : null;
        DateTime scheduledStart = obj.has("scheduled_start_time") && !obj.isNull("scheduled_start_time") ? DateTime.parse(obj.getString("scheduled_start_time")) : null;
        DateTime scheduledEnd = obj.has("scheduled_end_time") && !obj.isNull("scheduled_end_time") ? DateTime.parse(obj.getString("scheduled_end_time")) : null;
        PrivacyLevel privacyLevel = PrivacyLevel.fromValue(obj.getInt("privacy_level"));
        EventStatus eventStatus = EventStatus.fromValue(obj.getInt("status"));
        EntityType entityType = EntityType.fromValue(obj.getInt("entity_type"));
        String entityId = obj.has("entity_id") && !obj.isNull("entity_id") ? obj.getString("entity_id") : null;
        EntityMetadata entityMetadata = obj.has("entity_metadata") && !obj.isNull("entity_metadata") ? EntityMetadata.decompile(obj.getJSONObject("entity_metadata")) : null;
        User creator = obj.has("creator") && !obj.isNull("creator") ? (User) ModelDecoder.decodeObject(obj.getJSONObject("creator"), User.class, jar) : null;
        int userCount = obj.has("user_count") && !obj.isNull("user_count") ? obj.getInt("user_count") : 0;
        String coverHash = obj.has("cover_image_hash") && !obj.isNull("cover_image_hash") ? obj.getString("cover_image_hash") : null;
        return new ScheduledEvent(id, guildId, channelId, creatorId, name, description, scheduledStart, scheduledEnd, privacyLevel, eventStatus, entityType, entityId, entityMetadata, creator, userCount, coverHash);
    }

    @Override
    public StringFormatter formatter() {
        return new StringFormatter("guild-events", this.id, this.coverHash);
    }

    @Override
    public String iconHash() {
        return this.coverHash;
    }

    public static class EntityMetadata implements Compilerable {

        // Location of the event (1-100 characters)
        private String location;

        public EntityMetadata(String location) {
            this.location = location;
        }

        public String location() {
            return this.location;
        }

        @Override
        public JSONObject compile() {
            return new JSONObject()
                    .put("location", this.location);
        }

        public static EntityMetadata decompile(JSONObject obj) {
            return new EntityMetadata(obj.getString("location"));
        }
    }

    public enum EntityType {
        STAGE_INSTANCE(1),
        VOICE(2),
        EXTERNAL(3),
        UNKNOWN(-1)
        ;

        private int value;

        EntityType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EntityType fromValue(int value) {
            for (EntityType entityType : values()) {
                if (entityType.getValue() == value) return entityType;
            }
            return UNKNOWN;
        }
    }

    public enum EventStatus {
        SCHEDULED(1),
        ACTIVE(2),
        COMPLETED(3),
        CANCELLED(4),

        UNKNOWN(-1),
        ;

        private int value;

        EventStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static EventStatus fromValue(int value) {
            for (EventStatus eventStatus : values()) {
                if (eventStatus.getValue() == value) return eventStatus;
            }
            return UNKNOWN;
        }
    }

    public enum PrivacyLevel {

        GUILD_ONLY(2),
        UNKNOWN(-1)
        ;

        private int value;

        PrivacyLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static PrivacyLevel fromValue(int value) {
            for (PrivacyLevel privacyLevel : values()) {
                if (privacyLevel.getValue() == value) return privacyLevel;
            }
            return UNKNOWN;
        }
    }

}
