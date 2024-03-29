package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

public class GuildChannelImpl extends ChannelImpl implements GuildChannel {

    private final Guild guild;
    private final int position;
    private final List<PermissionOverwrite> permissionOverwrites;
    private final boolean nsfw;
    private final DiscordJar discordJar;

    public GuildChannelImpl(String id, ChannelType type, String name, Guild guild, int position, List<PermissionOverwrite> permissionOverwrites, boolean nsfw, JSONObject raw, DiscordJar discordJar) {
        super(id, type, name, raw, discordJar);
        this.guild = guild;
        this.position = position;
        this.permissionOverwrites = permissionOverwrites;
        this.nsfw = nsfw;
        this.discordJar = discordJar;
    }

    @Override
    public @NotNull Guild guild() {
        if (this.guild == null) {

        }
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
    public DiscordJar discordJv() {
        return this.discordJar;
    }
}
