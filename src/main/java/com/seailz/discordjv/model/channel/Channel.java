package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.channel.ModifyBaseChannelAction;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.internal.ChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.utils.Mentionable;
import com.seailz.discordjv.utils.Snowflake;
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
    DiscordJv djv();

    @Override
    default String getMentionablePrefix() {
        return "#";
    }


    @NotNull
    @Contract("_, _ -> new")
    static Channel decompile(@NotNull JSONObject obj, DiscordJv discordJv) {
        return new ChannelImpl(
                obj.getString("id"),
                ChannelType.fromCode(obj.getInt("type")),
                obj.has("name") ? obj.getString("name") : "DM",
                obj, discordJv
        );
    }

    @NotNull
    JSONObject raw();

    default ModifyBaseChannelAction modify() {
        return new ModifyBaseChannelAction(djv(), id());
    }
}
