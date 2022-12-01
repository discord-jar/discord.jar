package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.channel.utils.ChannelType;
import com.seailz.discordjv.model.resolve.Resolvable;
import com.seailz.discordjv.utils.Mentionable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Discord channel
 *
 * @author Seailz
 * @since  1.0
 */
public interface Channel1 extends Compilerable, Resolvable, Mentionable {

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
}
