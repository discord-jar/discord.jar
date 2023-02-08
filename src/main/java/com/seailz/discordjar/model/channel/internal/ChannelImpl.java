package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.Channel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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

    @NotNull
    @Override
    public DiscordJar djv() {
        return discordJar;
    }
}
