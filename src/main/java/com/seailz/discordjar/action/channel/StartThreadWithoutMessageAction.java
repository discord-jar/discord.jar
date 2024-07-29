package com.seailz.discordjar.action.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.thread.Thread;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class StartThreadWithoutMessageAction {

    private String name;
    private Thread.AutoArchiveDuration autoArchiveDuration;
    private Thread.Type type;
    private boolean invitable = true;
    private int rateLimitPerUser;

    private DiscordJar djv;
    private String channelId;

    public StartThreadWithoutMessageAction(String name, Thread.Type type, DiscordJar djv, String channelId) {
        this.name = name;
        this.type = type;
        this.djv = djv;
        this.channelId = channelId;
    }

    public StartThreadWithoutMessageAction setAutoArchiveDuration(Thread.AutoArchiveDuration autoArchiveDuration) {
        this.autoArchiveDuration = autoArchiveDuration;
        return this;
    }

    public StartThreadWithoutMessageAction setInvitable(boolean invitable) {
        this.invitable = invitable;
        return this;
    }

    public StartThreadWithoutMessageAction setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
        return this;
    }

    public CompletableFuture<Channel> run() {
        CompletableFuture<Channel> future = new CompletableFuture<>();
        future.completeAsync(() -> {
            JSONObject body = new JSONObject();
            body.put("name", name);
            body.put("type", type.getCode());
            if (autoArchiveDuration != null) {
                body.put("auto_archive_duration", autoArchiveDuration.minutes());
            }
            body.put("invitable", invitable);

            if (rateLimitPerUser > 0) {
                body.put("rate_limit_per_user", rateLimitPerUser);
            }

            DiscordResponse response = null;
            try {
                response = new DiscordRequest(
                        body,
                        new HashMap<>(),
                        URLS.POST.CHANNELS.START_THREAD.replace("{channel.id}", channelId),
                        djv,
                        URLS.POST.CHANNELS.START_THREAD,
                        RequestMethod.POST
                ).invoke();
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                future.completeExceptionally(e);
                return null;
            }

            return Thread.decompile(response.body(), djv);
        });
        return future;
    }

}
