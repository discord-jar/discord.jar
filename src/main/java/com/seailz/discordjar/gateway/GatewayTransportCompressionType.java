package com.seailz.discordjar.gateway;

/**
 * Types of <a href="https://discord.com/developers/docs/topics/gateway#transport-compression">transport compression</a> types for the Gateway.
 * @author Seailz
 */
public enum GatewayTransportCompressionType {

    ZLIB_STREAM("zlib-stream"),
    NONE(null);

    private final String value;

    GatewayTransportCompressionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
