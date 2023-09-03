package com.seailz.discordjar.action.guild.scheduledevents;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.guild.scheduledevents.ScheduledEvent;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;

public class CreateScheduledEventAction {
    private final String name;
    private final ScheduledEvent.PrivacyLevel privacyLevel;
    private final DateTime startTime;
    private final ScheduledEvent.EntityType entityType;
    private final String guildId;
    private final DiscordJar discordJar;
    private String channelId;
    private ScheduledEvent.EntityMetadata entityMetadata;
    private DateTime endTime;
    private String description;
    private File image;

    public CreateScheduledEventAction(String name, ScheduledEvent.PrivacyLevel privacyLevel, DateTime startTime, ScheduledEvent.EntityType entityType, String guildId, DiscordJar discordJar) {
        this.name = name;
        this.privacyLevel = privacyLevel;
        this.startTime = startTime;
        this.entityType = entityType;
        this.guildId = guildId;
        this.discordJar = discordJar;
    }

    public CreateScheduledEventAction setDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateScheduledEventAction setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public CreateScheduledEventAction setChannel(Channel channel) {
        this.channelId = channel.id();
        return this;
    }

    public CreateScheduledEventAction setEndTime(DateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public CreateScheduledEventAction setEntityMetadata(ScheduledEvent.EntityMetadata entityMetadata) {
        this.entityMetadata = entityMetadata;
        return this;
    }

    public CreateScheduledEventAction setImage(File image) {
        this.image = image;
        return this;
    }

    public Response<ScheduledEvent> run() {
        Response<ScheduledEvent> res = new Response<>();

        new Thread(() -> {
            if (this.channelId == null && entityType != ScheduledEvent.EntityType.EXTERNAL) {
                res.completeError(new Response.Error(
                        0,
                        "Channel ID must be set for non-external events",
                        new JSONObject()
                ));
                return;
            }

            if ((this.entityMetadata == null || this.endTime == null) && entityType == ScheduledEvent.EntityType.EXTERNAL) {
                res.completeError(new Response.Error(
                        0,
                        "End time & entity metadata must be set for external events",
                        new JSONObject()
                ));
                return;
            }

            try {
                ScheduledEvent chan = ScheduledEvent.decompile(
                        new DiscordRequest(
                                new JSONObject()
                                        .put("name", name)
                                        .put("privacy_level", privacyLevel.getValue())
                                        .put("scheduled_start_time", startTime.toString())
                                        .put("entity_type", entityType.getValue())
                                        .put("channel_id", channelId)
                                        .put("entity_metadata", entityMetadata == null ? null : entityMetadata.compile())
                                        .put("scheduled_end_time", endTime == null ? null : endTime.toString())
                                        .put("description", description)
                                        .put("image", image == null ? null : image),
                                new HashMap<>(),
                                URLS.POST.GUILDS.SCHEDULED_EVENTS.CREATE_GUILD_SCHEDULED_EVENT.replace("{guild.id}", guildId),
                                discordJar,
                                URLS.POST.GUILDS.SCHEDULED_EVENTS.CREATE_GUILD_SCHEDULED_EVENT,
                                RequestMethod.POST
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
