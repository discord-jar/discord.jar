package com.seailz.discordjar.model.channel;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.channel.ModifyBaseChannelAction;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.channel.internal.ChannelImpl;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.resolve.Resolvable;
import com.seailz.discordjar.utils.Mentionable;
import com.seailz.discordjar.utils.Snowflake;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
}
