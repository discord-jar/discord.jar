package com.seailz.discordjar.voice.model.provider;

import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.voice.udp.VoiceUDP;
import com.seailz.discordjar.voice.ws.VoiceGatewayFactory;

/**
 * Extend this class to provide audio data to the voice gateway.
 * @author Seailz
 */
public abstract class VoiceProvider {

    private VoiceUDP udp;

    public void setUdp(VoiceUDP udp) {
        this.udp = udp;
    }

    /**
     * Drops the voice connection & closes the UDP socket. This will disconnect the bot from the voice channel.
     */
    public void disconnect() {
        udp.disconnect();
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

    /**
     * discord.jar will request from Discord to use this DAVE version. If set to 0, discord.jar will request to disable DAVE. Called before connecting to the voice gateway.
     */
    public int requestDaveVersion() {
        return VoiceGatewayFactory.LATEST_DAVE_VER;
    }

}
