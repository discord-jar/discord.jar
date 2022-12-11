package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.internal.ChannelImpl;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.utils.Mentionable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents a Discord channel
 *
 * @author Seailz
 * @since  1.0
 */
public interface Channel extends Compilerable, Resolvable, Mentionable {

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

    /**
     * Returns the channel as a mention
     */
    @NotNull
    @Override
    default String getAsMention() {
        return "<#" + id() + ">";
    }

    @NotNull
    @Contract("_ -> new")
    static Channel decompile(@NotNull JSONObject obj) {
        return new ChannelImpl(
                obj.getString("id"),
                ChannelType.fromCode(obj.getInt("type")),
                obj.getString("name")
        );
    }
}
