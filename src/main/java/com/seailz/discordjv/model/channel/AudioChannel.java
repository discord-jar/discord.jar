package com.seailz.discordjv.model.channel;

import com.seailz.discordjv.model.channel.audio.VoiceRegion;
import com.seailz.discordjv.model.message.Message;

/**
 * Represents an audio channel.
 */
public interface AudioChannel extends GuildChannel, CategoryMember {
    /**
     * Returns the ID of the last {@link Message} sent in the text section
     * <br>of the voice channel.
     */
    String lastMessageId();
    /**
     * Returns the {@link com.seailz.discordjv.model.channel.audio.VoiceRegion VoiceRegion} of the audio channel.
     */
    VoiceRegion region();
    /**
     * Returns the bitrate of the audio channel.
     */
    int bitrate();
}
