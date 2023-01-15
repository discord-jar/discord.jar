package com.seailz.discordjv.action.guild;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.scheduledevents.ScheduledEvent;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.timestamp.ISO8601;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class CreateScheduledEventAction {

    private final String name;
    private final ScheduledEvent.PrivacyLevel privacyLevel;
    private final ScheduledEvent.EntityType entityType;
    private final ISO8601 scheduledStartTime;
    private final String guildId;
    private final DiscordJv discordJv;

    private String channelId;
    private ScheduledEvent.EntityMetadata entityMetadata;
    private ISO8601 scheduledEndTime;
    private String description;
    private File image;

    public CreateScheduledEventAction(String name, ScheduledEvent.PrivacyLevel privacyLevel, ScheduledEvent.EntityType entityType, ISO8601 scheduledStartTime, String guildId, DiscordJv discordJv) {
        this.name = name;
        this.privacyLevel = privacyLevel;
        this.entityType = entityType;
        this.scheduledStartTime = scheduledStartTime;
        this.guildId = guildId;
        this.discordJv = discordJv;
    }

    public CreateScheduledEventAction setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEntityMetadata(ScheduledEvent.EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public ISO8601 getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(ISO8601 scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public ScheduledEvent.EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    public ScheduledEvent.EntityType getEntityType() {
        return entityType;
    }

    public File getImage() {
        return image;
    }

    public ISO8601 getScheduledStartTime() {
        return scheduledStartTime;
    }

    public ScheduledEvent.PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public CompletableFuture<ScheduledEvent> run() {
        if (channelId == null && entityType != ScheduledEvent.EntityType.EXTERNAL) {
            throw new IllegalStateException("Channel id must be set if the entity type is not EXTERNAL");
        }
        if (scheduledEndTime == null && entityType == ScheduledEvent.EntityType.EXTERNAL) {
            throw new IllegalStateException("Scheduled end time must be set if the entity type is EXTERNAL");
        }
        if (entityMetadata == null && entityType == ScheduledEvent.EntityType.EXTERNAL) {
            throw new IllegalStateException("Entity metadata must be set if the entity type is STAGE_INSTANCE");
        }
        CompletableFuture<ScheduledEvent> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            String imageData = null;
            if (getImage() != null) {
                try {
                    imageData =
                            "data:" + Files.probeContentType(getImage().toPath()) + ";base64," + java.util.Base64.getEncoder().encodeToString(Files.readAllBytes(getImage().toPath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            JSONObject payload = new JSONObject();
            if (getChannelId() != null) {
                payload.put("channel_id", getChannelId());
            }

            if (getEntityMetadata() != null) {
                payload.put("entity_metadata", getEntityMetadata().compile());
            }

            if (getScheduledEndTime() != null) {
                payload.put("scheduled_end_time", getScheduledEndTime().toString());
            }

            if (getDescription() != null) {
                payload.put("description", getDescription());
            }

            if (getImage() != null) {
                payload.put("image", imageData);
            }

            payload.put("name", getName());
            payload.put("privacy_level", getPrivacyLevel().getValue());
            payload.put("entity_type", getEntityType().getValue());
            payload.put("scheduled_start_time", getScheduledStartTime().toString());

            DiscordRequest req = new DiscordRequest(
                    payload,
                    new HashMap<>(),
                    URLS.POST.GULDS.SCHEDULED_EVENTS.CREATE_SCHEDULED_EVENTS
                            .replace("{guild.id}", guildId),
                    discordJv,
                    URLS.POST.GULDS.SCHEDULED_EVENTS.CREATE_SCHEDULED_EVENTS,
                    RequestMethod.POST
            );
            return ScheduledEvent.decompile(req.invoke().body(), discordJv);
        });
        return future;
    }
}
