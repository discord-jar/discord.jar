package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.Channel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import org.jetbrains.annotations.Contract;
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
    private final DiscordJv discordJv;

    public ChannelImpl(String id, ChannelType type, String name, JSONObject raw, DiscordJv discordJv) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.raw = raw;
        this.discordJv = discordJv;
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
    public DiscordJv djv() {
        return discordJv;
    }
}
