package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.Channel1;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Impl of {@link Channel1}
 */
public class ChannelImpl implements Channel1 {

    private final String id;
    private final ChannelType type;
    private final String name;

    public ChannelImpl(String id, ChannelType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    @NotNull
    @Contract("_ -> new")
    public static ChannelImpl decompile(@NotNull JSONObject obj) {
        return new ChannelImpl(
                obj.getString("id"),
                ChannelType.fromCode(obj.getInt("type")),
                obj.getString("name")
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
}
