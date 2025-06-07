package com.seailz.discordjar.voice.model.packet;

import com.codahale.xsalsa20poly1305.SecretBox;
import com.codahale.xsalsa20poly1305.SimpleBox;
import com.google.crypto.tink.aead.internal.InsecureNonceAesGcmJce;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class AudioPacket {

    private byte[] data;
    private final byte[] header;
    private byte[] counterBytes = new byte[0];

    public AudioPacket(byte[] data, int ssrc, char sequence, int timestamp) {
        this.header = ByteBuffer.allocate(12)
                .put(0, (byte) 0x80) // Version & Flags
                .put(1, (byte) 0x78) // Payload Type
                .putChar(2, sequence)
                .putInt(4, timestamp)
                .putInt(8, ssrc)
                .array();
        this.data = data;
    }

    /**
     * Encrypts {@link #data} in‑place with AES‑256‑GCM
     *
     * @param secretKey 32‑byte key from SESSION_DESCRIPTION
     * @param counter   monotonically increasing 32‑bit integer (wrap → renegotiate)
     */
    @SneakyThrows
    public void encrypt(byte[] secretKey, int counter) {
//        System.out.println("Encoding packet with counter: " + counter + "and secret key: " + secretKey.length);
//        // Build 12‑byte IV = 8 × 0x00 || counter (little‑endian)
//        byte[] iv = new byte[12];
//        ByteBuffer.wrap(iv, 8, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(counter);
//
//        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secretKey, "AES"),
//                new GCMParameterSpec(128, iv)); // 128‑bit auth tag
//
//        // Authenticate header so any bit‑flip is detected on receive
//        System.out.println("Header: " + header.length);
//        cipher.updateAAD(header);
//
//        // Ciphertext followed by 16‑byte GCM tag
//        this.data = cipher.doFinal(data);
//
//        // Store counter so we can append it after ciphertext when serialising
//        this.counterBytes = ByteBuffer.allocate(4)
//                .order(ByteOrder.LITTLE_ENDIAN)
//                .putInt(counter)
//                .array();


        byte[] iv = new byte[12];
        ByteBuffer.wrap(iv, 8, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(counter);
        this.counterBytes = ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(counter)
                .array();

        byte[] encrypted;
        try {
            var aesGcmJce = new InsecureNonceAesGcmJce(secretKey);

            encrypted = aesGcmJce.encrypt(iv, data, new byte[12]);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        this.data = encrypted;
    }

    public DatagramPacket toDatagramPacket(InetSocketAddress address) {
        int len = header.length + data.length + counterBytes.length;
        byte[] packet = new byte[len];
        System.arraycopy(header, 0, packet, 0, header.length);
        System.arraycopy(data, 0, packet, header.length, data.length);
        System.arraycopy(counterBytes, 0, packet, header.length + data.length, counterBytes.length);
        return new DatagramPacket(packet, packet.length, address);
    }

}
