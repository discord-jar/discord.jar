package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.model.channel.GuildChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuildChannelImpl extends ChannelImpl implements GuildChannel {

    private final Guild guild;
    private final int position;
    private final String topic;
    private final List<PermissionOverwrite> permissionOverwrites;

    public GuildChannelImpl(String id, ChannelType type, String name, Guild guild, int position, String topic, List<PermissionOverwrite> permissionOverwrites) {
        super(id, type, name);

        this.guild = guild;
        this.position = position;
        this.topic = topic;
        this.permissionOverwrites = permissionOverwrites;
    }

    @Override
    public @NotNull Guild guild() {
        return null;
    }

    // Will return 0 if not found
    @Override
    public int position() {
        return 0;
    }

    @Override
    public @NotNull String topic() {
        return null;
    }

    @Nullable
    @Override
    public List<PermissionOverwrite> permissionOverwrites() {
        return null;
    }

}
