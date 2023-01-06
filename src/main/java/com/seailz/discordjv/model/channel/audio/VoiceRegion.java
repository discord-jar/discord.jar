package com.seailz.discordjv.model.channel.audio;

import com.seailz.discordjv.model.channel.audio.internal.VoiceRegionImpl;
import com.seailz.discordjv.utils.Snowflake;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Represents a region for a voice channel.
 * <br>Used to specify the location of the voice server.
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.channel.AudioChannel
 * @see <a href="https://discordapp.com/developers/docs/resources/voice#voice-region-object">Voice Region Object</a>
 * @since 1.0
 */
public interface VoiceRegion extends Snowflake {

    /**
     * Unique ID for the region.
     */
    String id();

    /**
     * Name of the region.
     */
    String name();

    /**
     * True for a single server that is closest to the current user's client
     */
    boolean optimal();

    /**
     * Whether this is a deprecated voice region (avoid switching to these)
     */
    boolean deprecated();

    /**
     * Whether this is a custom voice region (used for events/etc)
     */
    boolean custom();

    @NotNull
    @Contract("_ -> new")
    static VoiceRegion decompile(@NotNull JSONObject obj) {
        String id = obj.getString("id");
        String name = obj.getString("name");
        boolean optimal = obj.getBoolean("optimal");
        boolean deprecated = obj.getBoolean("deprecated");
        boolean custom = obj.getBoolean("custom");

        return new VoiceRegionImpl(id, name, optimal, deprecated, custom);
    }
}
