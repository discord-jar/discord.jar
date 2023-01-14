package com.seailz.discordjv.http;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import software.pando.crypto.nacl.Crypto;

import java.nio.charset.StandardCharsets;

public class SecurityManager {

    public static boolean verify(String publicKey, String signature, String timestamp, String body) throws DecoderException {
        return Crypto.signVerify(
                Crypto.signingPublicKey(Hex.decodeHex(publicKey)),
                (timestamp + body).getBytes(StandardCharsets.UTF_8),
                Hex.decodeHex(signature));
    }
}