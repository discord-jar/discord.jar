package com.seailz.discordjar.voice.udp;

import com.seailz.discordjar.voice.model.packet.AudioPacket;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;

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
    private boolean sending = false;

    public VoiceUDP(InetSocketAddress address, VoiceProvider provider, int srrc) throws SocketException {
        this.address = address;
        this.provider = provider;
        this.ssrc = srrc;
        this.socket = new DatagramSocket();
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public InetSocketAddress discoverAddress() throws IOException {
        byte[] buffer = new byte[74];
        ByteBuffer.wrap(buffer).putShort((short) 1).putShort((short) 70).putInt(ssrc);
        socket.send(new DatagramPacket(buffer, buffer.length, address));
        buffer = new byte[74];
        socket.receive(new DatagramPacket(buffer, buffer.length));

        String ip = new String(buffer, 8, buffer.length - 10).trim();
        int port = ByteBuffer.wrap(new byte[] {buffer[buffer.length - 1], buffer[buffer.length - 2]}).getShort() & 0xffff;
        this.address = new InetSocketAddress(ip, port);
        System.out.println("Discovered address: " + address.getHostName() + ":" + address.getPort());
        return address;
    }

    public void start() throws IOException {
        System.out.println(socket.isConnected());
        sending = true;
        new Thread(() -> {
            long nextFrameTimestamp = System.nanoTime();
            while (sending) {
                if (!provider.canProvide()) {
                    //System.out.println("Cannot provide!");
                    continue;
                }
                byte[] data = provider.provide20ms();
                if (data == null) {
                    continue;
                }

                AudioPacket packet = new AudioPacket(data, ssrc, sequence, ((int) sequence) * 960);
                if (secretKey == null) {
                    Logger.getLogger("VoiceUDPConnection (discord.jar)").severe("Secret key is set to null, cannot encrypt audio packet. This is a bug, packet will be skipped.");
                    continue;
                }

                packet.encrypt(secretKey);
                if (socket.isClosed()) {
                    System.out.println("Socket was closed!");
                    continue;
                }

                nextFrameTimestamp = nextFrameTimestamp + 20_000_000;
                try {
                    Thread.sleep(Math.max(0, (nextFrameTimestamp - System.nanoTime()) / 1_000_000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println(Arrays.toString(packet.toDatagramPacket(address).getData()));

                try {
                    socket.send(packet.toDatagramPacket(address));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    public void stop() {
        sending = false;
    }




}
