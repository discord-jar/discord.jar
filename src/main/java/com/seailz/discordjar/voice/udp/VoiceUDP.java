package com.seailz.discordjar.voice.udp;

import com.codahale.xsalsa20poly1305.SecretBox;
import com.seailz.discordjar.voice.model.packet.AudioPacket;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import com.seailz.discordjar.voice.ws.VoiceGatewayFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

public class VoiceUDP {

    private DatagramSocket socket;
    private VoiceProvider provider;
    private InetSocketAddress address;
    private int ssrc;

    private byte[] secretKey;
    private char sequence = 0;
    private volatile boolean sending = false;
    private volatile boolean speaking = false;
    private final VoiceGatewayFactory voiceGateway;

    public VoiceUDP(InetSocketAddress address, VoiceProvider provider, int srrc, VoiceGatewayFactory voiceGateway) throws SocketException {
        this.address = address;
        this.provider = provider;
        provider.setUdp(this);
        this.ssrc = srrc;
        this.socket = new DatagramSocket();
        this.voiceGateway = voiceGateway;
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public InetSocketAddress discoverAddress() throws IOException {
        socket.connect(this.address);
        byte[] buffer = new byte[74];
        InetSocketAddress address = this.address;
        ByteBuffer.wrap(buffer).putShort((short) 1).putShort((short) 70).putInt(ssrc);
        socket.send(new DatagramPacket(buffer, buffer.length, address));
        buffer = new byte[74];
        socket.receive(new DatagramPacket(buffer, buffer.length));

        String ip = new String(buffer, 8, buffer.length - 10).trim();
        int port = ByteBuffer.wrap(new byte[] {buffer[buffer.length - 1], buffer[buffer.length - 2]}).getShort() & 0xffff;
        address = new InetSocketAddress(ip, port);
        return address;
    }

    public void start() throws IOException {
        if (!socket.isConnected()) {
            socket.connect(address);
        }
        sending = true;
        new Thread(() -> {
            long nextFrameTimestamp = System.nanoTime();
            while (sending) {
                if (!provider.canProvide()) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                byte[] frame = provider.provide20ms();
                if (frame == null) {
                    continue;
                }

                AudioPacket packet = null;

                if (!speaking && frame != null) {
                    speaking = true;
                    voiceGateway.speaking(true);
                }
                packet = new AudioPacket(frame, ssrc, sequence, ((int) sequence) * 960);

                if (secretKey == null) {
                    Logger.getLogger("VoiceUDPConnection (discord.jar)").severe("Secret key is set to null, cannot encrypt audio packet. This is a bug, packet will be skipped.");
                    continue;
                }

                if (packet != null) {
                    packet.encrypt(secretKey);
                }
                if (socket.isClosed()) {
                    continue;
                }

                nextFrameTimestamp = nextFrameTimestamp + 20_000_000;
                try {
                    Thread.sleep(Math.max(0, (nextFrameTimestamp - System.nanoTime()) / 1_000_000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (sequence + 1 > Character.MAX_VALUE)
                    sequence = 0;
                else
                    sequence++;


                if (packet != null) {
                    try {
                        socket.send(packet.toDatagramPacket(address));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }).start();
    }

    public void disconnect() {
        voiceGateway.close(1001);
        socket.disconnect();
    }

    public void stop() {
        sending = false;
    }




}
