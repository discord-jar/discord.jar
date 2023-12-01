package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.message.Message;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;

/**
 * Impl of {@link Channel}
 */
public class ChannelImpl implements Channel {

    private final String id;
    private final ChannelType type;
    private final String name;
    private final JSONObject raw;
    private final DiscordJar discordJar;

    public ChannelImpl(String id, ChannelType type, String name, JSONObject raw, DiscordJar discordJar) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.raw = raw;
        this.discordJar = discordJar;
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("type", type.getCode())
                .put("name", name);
    }

    @NotNull
    @Override
    public String id() {
        return id;
    }

    @NotNull
    @Override
    public ChannelType type() {
        return type;
    }

    @NotNull
    @Override
    public String name() {
        return name;
    }

    @NotNull
    @Override
    public JSONObject raw() {
        return raw;
    }

    @Override
    public Response<Void> delete() {
        Response<Void> response = new Response<>();
        new Thread(() -> {
            DiscordRequest req = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.DELETE.CHANNEL.DELETE_CHANNEL.replace("{channel.id}", id()),
                    djv(),
                    URLS.DELETE.CHANNEL.DELETE_CHANNEL,
                    RequestMethod.DELETE
            );
            try {
                req.invoke();
                response.complete(null);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                response.completeError(new Response.Error(e));
            }
        }, "djar--channel-delete").start();
        return response;
    }

    @NotNull
    @Override
    public DiscordJar djv() {
        return discordJar;
    }
}
