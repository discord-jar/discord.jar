package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.channel.ModifyBaseChannelAction;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.internal.ChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.Checker;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Represents a Discord channel
 *
 * @author Seailz
 * @since 1.0
 */
public interface Channel extends Compilerable, Resolvable, Mentionable, Snowflake {

    /**
     * The id of the channel
     */
    @NotNull
    String id();

    /**
     * The {@link ChannelType type} of this channel
     */
    @NotNull
    ChannelType type();

    /**
     * The name of the channel (1-100 characters)
     */
    @NotNull
    String name();

    @NotNull
    DiscordJar djv();

    @Override
    default String getMentionablePrefix() {
        return "#";
    }


    @NotNull
    @Contract("_, _ -> new")
    static Channel decompile(@NotNull JSONObject obj, DiscordJar discordJar) {
        return new ChannelImpl(
                obj.getString("id"),
                ChannelType.fromCode(obj.getInt("type")),
                obj.has("name") ? obj.getString("name") : "DM",
                obj, discordJar
        );
    }

    @NotNull
    JSONObject raw();

    default ModifyBaseChannelAction modify() {
        return new ModifyBaseChannelAction(djv(), id());
    }

    /**
     * Returns this class as a {@link GuildChannel}, or null if it is not a guild channel.
     * @throws IllegalArgumentException If the channel is not a guild channel
     */
    @Nullable
    default GuildChannel asGuildChannel() {
        try {
            return GuildChannel.decompile(raw(), djv());
        } catch (Exception e) {
            Checker.check(true, "This channel is not a guild channel");
        }
        return null;
    }

    Response<Void> delete();
}
