package com.seailz.discordjv.model.channel.internal;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.model.channel.GuildChannel;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

public class GuildChannelImpl extends ChannelImpl implements GuildChannel {

    private final Guild guild;
    private final int position;
    private final List<PermissionOverwrite> permissionOverwrites;
    private final boolean nsfw;
    private final DiscordJv discordJv;

    public GuildChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, JSONObject raw, DiscordJv discordJv) {
        super(id, type, name, raw, discordJv);
        this.guild = guild;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.nsfw = nsfw;
        this.discordJv = discordJv;
    }

    @Override
    public @NotNull Guild guild() {
        return this.guild;
    }

    // Will return 0 if not found
    @Override
    public int position() {
        return this.position;
    }

    @Nullable
    @Override
    public List<PermissionOverwrite> permissionOverwrites() {
        return this.permissionOverwrites;
    }

    @Override
    public boolean nsfw() {
        return nsfw;
    }

    @NotNull
    @Override
    public DiscordJv discordJv() {
        return this.discordJv;
    }
}
