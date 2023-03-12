package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.internal.GuildChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.permission.PermissionOverwrite;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface GuildChannel extends Channel {

    @NotNull
    Guild guild();

    // Will return 0 if not found
    int position();

    @Nullable
    List<PermissionOverwrite> permissionOverwrites();

    boolean nsfw();

    @NotNull JSONObject raw();

    @Override
    default JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id());
        obj.put("type", type().getCode());
        obj.put("name", name());
        obj.put("guild_id", guild().id());
        obj.put("position", position());
        obj.put("nsfw", nsfw());

        if (permissionOverwrites() != null) {
            JSONArray array = new JSONArray();
            for (PermissionOverwrite overwrite : permissionOverwrites())
                array.put(overwrite.compile());
        }

        obj.put("permission_overwrites", permissionOverwrites());
        return obj;
    }

    /**
     * Decompile a {@link JSONObject} into a {@link GuildChannel}
     *
     * @param obj The {@link JSONObject} to decompile
     * @param discordJar The {@link DiscordJar} instance
     *
     * @return The {@link GuildChannel} instance
     */
    @NotNull
    @Contract("_, _ -> new")
    static GuildChannel decompile(@NotNull JSONObject obj, @NotNull DiscordJar discordJar) throws DiscordRequest.UnhandledDiscordAPIErrorException {
        String id = obj.getString("id");
        ChannelType type = ChannelType.fromCode(obj.getInt("type"));
        String name = obj.getString("name");
        Guild guild = obj.has("guild_id") ? discordJar.getGuildById(obj.getString("guild_id")) : null;
        int position = obj.has("position") ? obj.getInt("position") : 0;
        boolean nsfw = obj.has("nsfw") && obj.getBoolean("nsfw");

        List<PermissionOverwrite> permissionOverwrites = new ArrayList<>();
        if (obj.has("permission_overwrites")) {
            JSONArray array = obj.getJSONArray("permission_overwrites");
            for (int i = 0; i < array.length(); i++) {
                JSONObject overwrite = array.getJSONObject(i);
                permissionOverwrites.add(PermissionOverwrite.decompile(overwrite));
            }
        }

        return new GuildChannelImpl(id, type, name, guild, position, permissionOverwrites, nsfw, obj, discordJar);
    }

    @NotNull
    DiscordJar discordJv();

    /**
     * Returns this class as a {@link MessagingChannel}, or null if it is not a messaging channel.
     * @throws IllegalArgumentException If the channel is not a messaging channel
     */
    @Nullable
    default MessagingChannel asMessagingChannel() {
        try {
            return MessagingChannel.decompile(raw(), discordJv());
        } catch (Exception e) {
            Checker.check(true, "This channel is not a messaging channel");
        }
        return null;
    }

    /**
     * Adds a {@link PermissionOverwrite} to this channel
     * @param overwrite The {@link PermissionOverwrite} to add
     */
    default void addPermissionOverwrite(@NotNull PermissionOverwrite overwrite) {
        permissionOverwrites().add(overwrite);
        modify().setPermissionOverwrites(permissionOverwrites()).run();
    }
}
