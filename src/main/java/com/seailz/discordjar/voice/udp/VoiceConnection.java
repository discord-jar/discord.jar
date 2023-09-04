package com.seailz.discordjar.voice.udp;

import com.seailz.discordjar.voice.ws.VoiceGateway;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.List;

public class VoiceConnection {

    private final int ssrc;
    private final int port;
    private final String ip;
    private final VoiceGateway voiceGateway;
    private DatagramSocket socket = new DatagramSocket();
    private InetSocketAddress address;
    private byte[] secretKey;

    public VoiceConnection(int ssrc, int port, String ip, VoiceGateway voiceGateway) throws IOException {
        this.ssrc = ssrc;
        this.port = port;
        this.ip = ip;
        this.voiceGateway = voiceGateway;
        System.out.println("Starting: " + ip + ":" + port);
        discoverAddress();
    }

    public InetSocketAddress discoverAddress() throws IOException {
        socket.connect(new InetSocketAddress(ip, port));
        InetSocketAddress address = new InetSocketAddress(ip, port);
        byte[] buffer = new byte[74];
        ByteBuffer.wrap(buffer).putShort((short) 1).putShort((short) 70).putInt(ssrc);
        socket.send(new DatagramPacket(buffer, buffer.length, address));
        buffer = new byte[74];
        socket.receive(new DatagramPacket(buffer, buffer.length));

        String ip = new String(buffer, 8, buffer.length - 10).trim();
        int port = ByteBuffer.wrap(new byte[] {buffer[buffer.length - 1], buffer[buffer.length - 2]}).getShort() & 0xffff;
        address = new InetSocketAddress(ip, port);
        return address;
    }

    public void startConnection() {
        voiceGateway.speaking(ssrc, List.of(VoiceGateway.SpeakingFlags.MIC));
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (voiceGateway.getProvider().canProvide()) {

                }
            }
        }, "djar--voice-connection-" + ssrc).start();
    }

    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    public record IpPort(String ip, int port) {
    }


}
