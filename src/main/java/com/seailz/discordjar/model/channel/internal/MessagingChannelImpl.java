package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Category;
import com.seailz.discordjar.model.channel.MessagingChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.message.Message;
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

    private Category category;
    private final String ownerId;
    private final int slowMode;
    private final String topic;
    private final String lastMessageId;
    private final int defaultAutoArchiveDuration;
    private final DiscordJar discordJar;


    public MessagingChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, String ownerId, int slowMode, String topic, String lastMessageId, int defaultAutoArchiveDuration, DiscordJar discordJar, JSONObject raw) {
        super(id, type, name, guild, position, permissionOverwrites, nsfw, raw, discordJar);
        this.ownerId = ownerId;
        this.slowMode = slowMode;
        this.topic = topic;
        this.lastMessageId = lastMessageId;
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
        this.discordJar = discordJar;
    }

    @Override
    public Category owner() {
        if (category == null) category = discordJar.getCategoryById(ownerId);
        return category;
    }

    @Override
    public String parentId() {
        return ownerId;
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

        }, "djar--msg-channel-impl").start();
        return response;
    }

    @Override
    public Response<Message> crosspostMessage(@NotNull String message) {
        Response<Message> response = new Response<>();
        new Thread(() -> {
            DiscordRequest req = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.POST.MESSAGES.CROSSPOST_MESSAGE.replace("{channel.id}", id()),
                    djv(),
                    URLS.POST.MESSAGES.CROSSPOST_MESSAGE,
                    RequestMethod.POST
            );
            try {
                req.invoke();
                response.complete(Message.decompile(req.body(), djv()));
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                response.completeError(new Response.Error(e));
            }
        }, "djar--channel-crosspost-message").start();
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