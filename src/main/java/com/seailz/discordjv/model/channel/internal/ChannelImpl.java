package com.seailz.discordjv.model.channel.internal;

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

    public ChannelImpl(String id, ChannelType type, String name, JSONObject raw) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.raw = raw;
    }

    @NotNull
    @Contract("_ -> new")
    public static ChannelImpl decompile(@NotNull JSONObject obj) {
        return new ChannelImpl(
                obj.getString("id"),
                ChannelType.fromCode(obj.getInt("type")),
                obj.getString("name"), obj
        );
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
}
