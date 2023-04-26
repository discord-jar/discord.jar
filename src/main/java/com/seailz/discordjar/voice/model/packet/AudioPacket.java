package com.seailz.discordjar.voice.model.packet;

import com.codahale.xsalsa20poly1305.SecretBox;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class AudioPacket {

    private byte[] data;
    private byte[] header;

    public AudioPacket(byte[] data, int ssrc, char sequence, int timestamp) {
        ByteBuffer header = ByteBuffer.allocate(12)
                .put(0, (byte) 0x80)
                .put(1, (byte) 0x78)
                .putChar(2, sequence)
                .putInt(4, timestamp)
                .putInt(8, ssrc);
        this.header = header.array();
        this.data = data;
    }

    public void encrypt(byte[] secretKey) {
        byte[] nonce = new byte[24];
        System.arraycopy(header, 0, nonce, 0, 12);
        data = new SecretBox(secretKey).seal(nonce, data);
    }

    public DatagramPacket toDatagramPacket(InetSocketAddress address) {
        byte[] packetData = new byte[header.length + data.length];
        System.arraycopy(header, 0, packetData, 0, header.length);
        System.arraycopy(data, 0, packetData, header.length, data.length);
        return new DatagramPacket(packetData, packetData.length, address);
    }

}
