package com.seailz.discordjar.model.channel.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.GuildChannel;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.invite.Invite;
import com.seailz.discordjar.model.invite.internal.InviteImpl;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public Response<List<Invite>> retrieveChannelInvites() {
        Response<List<Invite>> response = new Response<>();
        new Thread(() -> {
            DiscordRequest req = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    URLS.GET.CHANNELS.GET_CHANNEL_INVITES.replace("{channel.id}", id()),
                    djv(),
                    URLS.GET.CHANNELS.GET_CHANNEL_INVITES,
                    RequestMethod.GET
            );
            try {
                JSONArray arr = req.invoke().arr();
                List<Invite> invites = new ArrayList<>();
                for (Object o : arr) {
                    JSONObject obj = (JSONObject) o;
                    invites.add(InviteImpl.decompile(obj, djv()));
                }
                response.complete(invites);
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException e) {
                response.completeError(new Response.Error(e));
            }
        }, "djar--channel-retrieve-invites").start();
        return response;
    }
}
