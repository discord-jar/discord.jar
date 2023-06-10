package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;

public class MessagingChannelImpl extends GuildChannelImpl implements MessagingChannel {

    private final Category category;
    private final int slowMode;
    private final String topic;
    private final String lastMessageId;
    private final int defaultAutoArchiveDuration;
    private final DiscordJar discordJar;


    public MessagingChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, Category owner, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJar discordJar, JSONObject raw) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.category = owner;
        this.slowMode = slowMode;
        this.topic = topic;
        this.lastMessageId = lastMessageId;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.discordJar = discordJar;
    }

    @Override
    public Category owner() {
        return null;
    }

    @Override
    public int slowMode() {
        return slowMode;
    }

    @Override
    public String topic() {
        return topic;
    }

    @Override
    public String lastMessageId() {
        return lastMessageId;
    }

    @Override
    public Response<Void> bulkDeleteMessages(List<String> messageIds, boolean filterMessages, String reason) {
        Response<Void> response = new Response<>();
        new Thread(() -> {
            HashMap<String, String> headers = new HashMap<>(){{
                if (reason != null) put("X-Audit-Log-Reason", reason);
            }};
            DiscordRequest request = new DiscordRequest(
                    new JSONObject(),
                    headers,
                    URLS.POST.CHANNELS.MESSAGES.BULK_DELETE
                            .replace("{channel.id}", id()),
                    discordJar,
                    URLS.POST.CHANNELS.MESSAGES.BULK_DELETE,
                    RequestMethod.POST
            );

            try {
                request.invoke();
                response.complete(null);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                response.completeError(
                        new Response.Error(e)
                );
            }

        }).start();
        return response;
    }

    @Override
    public int defaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    @Override
    public @NotNull DiscordJar discordJv() {
        return discordJar;
    }
}