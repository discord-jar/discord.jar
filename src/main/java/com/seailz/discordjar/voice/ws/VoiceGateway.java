package com.seailz.discordjar.voice.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.gateway.heartbeat.HeartLogic;
import com.seailz.discordjar.utils.flag.Bitwiseable;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import com.seailz.discordjar.voice.udp.VoiceConnection;
import com.seailz.discordjar.ws.ExponentialBackoffLogic;
import com.seailz.discordjar.ws.WebSocket;
import org.json.JSONObject;
import org.springframework.util.backoff.ExponentialBackOff;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.logging.Logger;

public class VoiceGateway {

    private final String sessionId;
    private final String token;
    private final String endpoint;
    private final WebSocket webSocket;
    private final VoiceProvider provider;
    private final boolean debug;
    private final DiscordJar jar;
    private VoiceConnection udpConnection;

    public VoiceGateway(String sessionId, String token, String endpoint, VoiceProvider provider, DiscordJar jar, boolean debug) {
        this.sessionId = sessionId;
        this.token = token;
        this.endpoint = endpoint;
        this.provider = provider;
        this.debug = debug;
        this.jar = jar;

        webSocket = new WebSocket("wss://" + endpoint + "?v=" + Version.latest().getCode(), debug);
        webSocket.connect();

        webSocket.addOnConnect(this::handleConnect);
        webSocket.addOnDisconnectConsumer((event) -> {
            Logger.getLogger("VOICE").info("Disconnected from voice gateway");
        });
        webSocket.addMessageConsumer(this::handleMessage);
        webSocket.addOnDisconnectConsumer((event) -> {
            Logger.getLogger("VOICE").info("Disconnected from voice gateway");
        });

        ExponentialBackoffLogic logic = new ExponentialBackoffLogic();
        webSocket.setReEstablishConnection(logic.getFunction());
    }

    private void handleConnect() {
        System.out.println("Connected to voice gateway");
        JSONObject gwPayload = new JSONObject();
        gwPayload.put("op", 0);
        JSONObject data = new JSONObject();
        data.put("server_id", provider.guild().id());
        data.put("user_id", jar.getSelfUser().id());
        data.put("session_id", sessionId);
        data.put("token", token);
        gwPayload.put("d", data);
        webSocket.send(gwPayload.toString());
    }

    private synchronized void handleMessage(String msg) {
        JSONObject payload = new JSONObject(msg);
        JSONObject inner = payload.getJSONObject("d");
        if (debug) Logger.getLogger("VOICE").info("[Voice GW] " + payload);

        if (payload.getInt("op") == 2) {
            System.out.println("Voice gateway ready");
            int ssrc = inner.getInt("ssrc");
            int port = inner.getInt("port");
            String ip = inner.getString("ip");

            try {
                this.udpConnection = new VoiceConnection(ssrc, port, ip, this);
                InetSocketAddress address = udpConnection.discoverAddress();

                JSONObject selectProtocol = new JSONObject();
                selectProtocol.put("op", 1);
                JSONObject data = new JSONObject();
                data.put("protocol", "udp");
                data.put("data", new JSONObject()
                        .put("address", address.getHostName())
                        .put("port", address.getPort())
                        .put("mode", "xsalsa20_poly1305"));
                selectProtocol.put("d", data);
                webSocket.send(selectProtocol.toString());
            } catch (IOException e) {
                System.out.println("Failed to create voice connection");
                e.printStackTrace();
            }
        } else if (payload.getInt("op") == 9) {
            new HeartLogic(webSocket, inner.getInt("heartbeat_interval")).start();
        } else if (payload.getInt("op") == 4) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode packet = null;
            try {
                packet = mapper.readTree(payload.toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            JsonNode data = packet.get("d");
            byte[] key = new ObjectMapper().convertValue(data.get("secret_key"), byte[].class);
            // Session Description! We're ready to start the voice connection.
            udpConnection.setSecretKey(key);
            udpConnection.startConnection();
        }
    }

    public void speaking(int ssrc, List<SpeakingFlags> flags) {
        int flagsRaw = 0;
        for (SpeakingFlags flag : flags) {
            flagsRaw |= flag.getLeftShiftId();
        }

        JSONObject speaking = new JSONObject();
        speaking.put("op", 5);
        speaking.put("d", new JSONObject()
                .put("speaking", flagsRaw)
                .put("delay", 0)
                .put("ssrc", ssrc));

        webSocket.send(speaking.toString());
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public VoiceProvider getProvider() {
        return provider;
    }

    public enum SpeakingFlags implements Bitwiseable {

        MIC(0), // Normal transmission of voice audio
        SOUNDSHARE(1), // Transmission of context audio for video, no speaking indicator
        PRIORITY(2) // Priority speaker, lowering audio of other speakers
        ;

        private int id;

        SpeakingFlags(int id) {
            this.id = id;
        }

        @Override
        public int getLeftShiftId() {
            return 1 << id;
        }

        @Override
        public int id() {
            return id;
        }
    }

    public enum Version {
        V4(true, 4, true),
        V3(true, 3, false),
        V2(true, 2, false),
        V1(true, 1, false);

        private boolean available;
        private boolean supported;
        private int code;

        Version(boolean available, int code, boolean supported) {
            this.available = available;
            this.code = code;
            this.supported = supported;
        }

        public boolean isAvailable() {
            return this.available;
        }

        public int getCode() {
            return code;
        }

        public static Version latest() {
            Version latest = null;
            for (Version version : values()) {
                if (latest == null || version.getCode() > latest.getCode()) {
                    latest = version;
                }
            }
            return latest;
        }
    }

}
