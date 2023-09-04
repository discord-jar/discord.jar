package com.seailz.discordjar.voice.model.provider;

import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.voice.ws.VoiceGatewayFactory;

/**
 * Extend this class to provide audio data to the voice gateway.
 * @author Seailz
 */
public abstract class VoiceProvider {

    private Guild guild;
    private VoiceGatewayFactory voiceGateway;

    public VoiceProvider(Guild guild) {
        this.guild = guild;
    }

    /**
     * This method must be overridden to provide audio data to the voice gateway.
     *
     * @return the audio data to send to the voice gateway.
     */
    public abstract byte[] provide20ms();

    /**
     * This method must be overridden to provide a boolean defining whether you are currently speaking.
     */
    public abstract boolean canProvide();

}
