package com.seailz.discordjar.action.guild.scheduledevents;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.guild.scheduledevents.ScheduledEvent;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;

public class ModifyScheduledEventAction {

    private final String guildId;
    private final DiscordJar discordJar;
    private final String eventId;
    private String name;
    private ScheduledEvent.PrivacyLevel privacyLevel;
    private DateTime startTime;
    private ScheduledEvent.EntityType entityType;
    private String channelId;
    private ScheduledEvent.EntityMetadata entityMetadata;
    private DateTime endTime;
    private String description;
    private File image;
    private ScheduledEvent.EventStatus eventStatus;

    public ModifyScheduledEventAction(DiscordJar jar, String guildId, String eventId) {
        this.discordJar = jar;
        this.guildId = guildId;
        this.eventId = eventId;
    }

    public ModifyScheduledEventAction setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public ModifyScheduledEventAction setEntityMetadata(ScheduledEvent.EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
        return this;
    }

    public ModifyScheduledEventAction setDescription(String description) {
        this.description = description;
        return this;
    }

    public ModifyScheduledEventAction setEndTime(DateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public ModifyScheduledEventAction setEntityType(ScheduledEvent.EntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public ModifyScheduledEventAction setEventStatus(ScheduledEvent.EventStatus eventStatus) {
        this.eventStatus = eventStatus;
        return this;
    }

    public ModifyScheduledEventAction setImage(File image) {
        this.image = image;
        return this;
    }

    public ModifyScheduledEventAction setName(String name) {
        this.name = name;
        return this;
    }

    public ModifyScheduledEventAction setPrivacyLevel(ScheduledEvent.PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
        return this;
    }

    public ModifyScheduledEventAction setStartTime(DateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public Response<ScheduledEvent> run() {
        Response<ScheduledEvent> res = new Response<>();

        boolean pushChannelId = entityType != null && entityType == ScheduledEvent.EntityType.EXTERNAL;
        if (pushChannelId && entityMetadata == null) {
            res.completeError(new Response.Error(
                    0,
                    "You must provide an entityMetadata object when setting the entityType to EXTERNAL",
                    null
            ));
            return res;
        }

        if (pushChannelId && endTime == null) {
            res.completeError(new Response.Error(
                    0,
                    "You must provide an endTime when setting the entityType to EXTERNAL",
                    null
            ));
            return res;
        }

        new Thread(() -> {
            try {
                JSONObject obj = new JSONObject();
                if (name != null) obj.put("name", name);
                if (privacyLevel != null) obj.put("privacy_level", privacyLevel.getValue());
                if (startTime != null) obj.put("scheduled_start_time", startTime.toString());
                if (entityType != null) obj.put("entity_type", entityType.getValue());
                if (channelId != null && !pushChannelId) obj.put("channel_id", channelId);
                if (entityMetadata != null) obj.put("entity_metadata", entityMetadata.compile());
                if (endTime != null) obj.put("scheduled_end_time", endTime.toString());
                if (description != null) obj.put("description", description);
                if (image != null) obj.put("image", image);
                if (eventStatus != null) obj.put("status", eventStatus.getValue());
                // This is a PATCH request, so we must only send the fields that we want to change.

                ScheduledEvent chan = ScheduledEvent.decompile(
                        new DiscordRequest(
                                obj,
                                new HashMap<>(),
                                URLS.PATCH.GUILD.SCHEDULED_EVENTS.MODIFY_GUILD_SCHEDULED_EVENT.replace("{guild.id}", guildId).replace("{event.id}", eventId),
                                discordJar,
                                URLS.PATCH.GUILD.SCHEDULED_EVENTS.MODIFY_GUILD_SCHEDULED_EVENT,
                                RequestMethod.PATCH
                        ).invoke().body(),
                        discordJar
                );
                res.complete(chan);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                res.completeError(new Response.Error(
                        e.getCode(),
                        e.getMessage(),
                        e.getBody()
                ));
            }
        }).start();
        return res;
    }
}
