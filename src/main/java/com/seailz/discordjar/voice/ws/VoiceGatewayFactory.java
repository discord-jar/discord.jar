package com.seailz.discordjar.voice.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seailz.discordjar.voice.model.packet.AudioPacket;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import com.seailz.discordjar.voice.udp.VoiceUDP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.codahale.xsalsa20poly1305.SecretBox;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class VoiceGatewayFactory extends TextWebSocketHandler {

    private final String serverId;
    private final String userId;
    private final String sessionId;
    private final String token;

    private WebSocketSession session;
    private WebSocketClient client;

    private VoiceProvider provider;
    private int ssrc;

    private List<Consumer<JSONObject>> onSessionDescription = new ArrayList<>();
    private boolean speaking = true;
    private VoiceUDP socket;

    public VoiceGatewayFactory(String serverId, String userId, String sessionId, String token, String endpoint, VoiceProvider prov) throws ExecutionException, InterruptedException {
        this.serverId = serverId;
        this.userId = userId;
        this.sessionId = sessionId;
        this.token = token;
        this.provider = prov;
        connect(endpoint);
        System.out.println("Connected to voice gateway");
    }

    public void connect(String endpoint) throws ExecutionException, InterruptedException {
        endpoint = "wss://" + endpoint + "?v=4";
        WebSocketClient client = new StandardWebSocketClient();
        this.client = client;
        this.session = client.execute(this, new WebSocketHttpHeaders(), URI.create(endpoint)).get();
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
        System.out.println("Established connection to " + endpoint);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Closed: " + status.getCode() + " : " + status.getReason());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject payload = new JSONObject(message.getPayload());
        switch (payload.getInt("op")) {
            case 2: {
                payload = payload.getJSONObject("d");

                JSONObject finalPayload = payload;
                VoiceUDP udp = null;
                ssrc = finalPayload.getInt("ssrc");
                try {
                    System.out.println("IP:" + finalPayload.getString("ip") + " PORT:" + finalPayload.getInt("port") + " SSRC:" + finalPayload.getInt("ssrc") + " MODES:" + finalPayload.getJSONArray("modes"));
                    udp = new VoiceUDP(new InetSocketAddress(InetAddress.getByName(finalPayload.getString("ip")), finalPayload.getInt("port")), provider, finalPayload.getInt("ssrc"), this);
                } catch (SocketException | UnknownHostException e) {
                    throw new RuntimeException(e);
                }

                this.socket = udp;
                try {
                    sendSelectProtocol();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                speaking(false);
                Thread.sleep(1000);

                break;
            }
            case 8: {
                int hbInterval = payload.getJSONObject("d").getInt("heartbeat_interval");
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject firstHeartbeat = new JSONObject();
                    firstHeartbeat.put("op", 3);
                    long firstNonce = System.currentTimeMillis();
                    firstHeartbeat.put("d", firstNonce);
                    try {
                        session.sendMessage(new TextMessage(firstHeartbeat.toString()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while (true) {
                        try {
                            Thread.sleep(hbInterval);
                            JSONObject heartbeat = new JSONObject();
                            heartbeat.put("op", 3);
                            long nonce = System.currentTimeMillis();
                            heartbeat.put("d", nonce);
                            session.sendMessage(new TextMessage(firstHeartbeat.toString()));
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                JSONObject identify = new JSONObject();
                identify.put("op", 0);
                JSONObject data = new JSONObject();
                data.put("server_id", serverId);
                data.put("user_id", userId);
                data.put("session_id", sessionId);
                data.put("token", token);
                identify.put("d", data);
                try {
                    session.sendMessage(new TextMessage(identify.toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
            case 4: {
                speaking(true);

                ObjectMapper mapper = new ObjectMapper();
                JsonNode packet = mapper.readTree(payload.toString());
                System.out.println(payload);

                JsonNode data = packet.get("d");
                byte[] secretKey = new ObjectMapper().convertValue(data.get("secret_key"), byte[].class);

                socket.setSecretKey(secretKey);
                socket.start();
                break;
            }
            case 18: {
                break;
            }
        }
    }

    public void sendSelectProtocol() throws IOException {
        InetSocketAddress address = socket.discoverAddress();
        JSONObject selectProtocol = new JSONObject();
        selectProtocol.put("op", 1);
        JSONObject data = new JSONObject();
        data.put("protocol", "udp");
        data.put("data", new JSONObject()
                .put("address", address.getAddress().getHostAddress())
                .put("port", address.getPort())
                .put("mode", "xsalsa20_poly1305"));
        selectProtocol.put("d", data);
        System.out.println("Sending select protocol");
        System.out.println(selectProtocol.toString());
        session.sendMessage(new TextMessage(selectProtocol.toString()));
    }

    /*public void start(String ip, int ssrc, int port, List<String> modes, VoiceProvider vp) {
        try {
            // Open a UDP connection to the IP and port provided
            /*DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(ip);
            socket.connect(address, port);

            // Perform an IP discovery if required
            byte[] discoveryData = ByteBuffer.allocate(70).putLong(ssrc).array();
            System.out.println(Arrays.toString(discoveryData));
            socket.send(new DatagramPacket(discoveryData, discoveryData.length, address, port));
            System.out.println("Sent discovery packet");
            System.out.println(new DatagramPacket(discoveryData, discoveryData.length, address, port).toString());

            // Receive the external IP and UDP port from the server
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Waiting for packet");
            socket.receive(packet);
            System.out.println("Received packet");
            ByteBuffer bb = ByteBuffer.wrap(buffer);
            byte[] externalIpBytes = new byte[4];
            bb.get(externalIpBytes);
            String externalIp = InetAddress.getByAddress(externalIpBytes).getHostAddress();
            int externalPort = bb.getInt();
            System.out.println(externalPort);
            System.out.println(externalIp);

            String externalIp = ip;
            int externalPort = port;
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(ip);
            socket.connect(address, port);

           JSONObject selectProtocol = new JSONObject();
           selectProtocol.put("op", 1);
           JSONObject selectProtocolData = new JSONObject();
           selectProtocolData.put("protocol", "udp");
           selectProtocolData.put("data", new JSONObject().put("address", externalIp).put("port", externalPort).put("mode", "xsalsa20_poly1305"));
           selectProtocol.put("d", selectProtocolData);
           try {
               session.sendMessage(new TextMessage(selectProtocol.toString()));
           } catch (IOException e) {
               throw new RuntimeException(e);
           }

            System.out.println("SENT  + " + selectProtocol.toString());

            final char[] sequence = {(char) 0};
           onSessionDescription.add((data) -> {
               System.out.println("Starting voice connection");
               JSONArray encrpytionSecrets = data.getJSONArray("secret_key");
                byte[] secret = new byte[encrpytionSecrets.length()];
                for (int i = 0; i < encrpytionSecrets.length(); i++) {
                    secret[i] = (byte) encrpytionSecrets.getInt(i);
                }
                // Start encrypting and sending voice data
               new Thread(() -> {
                   while (true) {
                       try {
                           Thread.sleep(20);
                       } catch (InterruptedException e) {
                           throw new RuntimeException(e);
                       }
                       if (vp.canProvide() && !speaking) {
                           // Send a speaking payload to the WS
                           JSONObject speaking = new JSONObject();
                           speaking.put("op", 5);
                           JSONObject speakingData = new JSONObject();
                           speakingData.put("speaking", 1);
                           speakingData.put("delay", 0);
                           speakingData.put("ssrc", ssrc);
                           speaking.put("d", speakingData);
                           try {
                               session.sendMessage(new TextMessage(speaking.toString()));
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }
                           System.out.println("Sent speaking payload");
                           this.speaking = true;
                       }

                       /*if (!vp.canProvide() && speaking) {
                           // Send a speaking payload to the WS with speaking set to 0
                           JSONObject speaking = new JSONObject();
                           speaking.put("op", 5);
                           JSONObject speakingData = new JSONObject();
                           speakingData.put("speaking", 0);
                           speakingData.put("delay", 0);
                           speakingData.put("ssrc", ssrc);
                           speaking.put("d", speakingData);
                           try {
                               session.sendMessage(new TextMessage(speaking.toString()));
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }
                           System.out.println("Sent stop speaking payload");
                           this.speaking = false;
                       }


                           if (audioData.array() == null) continue;

                           // Construct RTP header
                           AudioPacket pack = new AudioPacket(audioData.array(), ssrc, sequence[0], ((int) sequence[0] * 960));
                           pack.encrypt(secret);
                           //DatagramPacket udpPacket = pack.toDatagramPacket();
                           //socket.send(udpPacket);
                           System.out.println("Sent packet");

                           // Increment sequence number
                           sequence[0]++;
                       }
                   }
               }).start();

           });



        } catch (IOException e) {
            System.out.println();
            throw new RuntimeException(e);
        }
    }*/

    public void speaking(boolean speak) {
        new Thread(() -> {
            JSONObject speaking = new JSONObject();
            speaking.put("op", 5);
            JSONObject speakingData = new JSONObject();
            speakingData.put("speaking", speak ? 5 : 0);
            speakingData.put("ssrc", ssrc);
            speakingData.put("delay", 0);
            speaking.put("d", speakingData);
            try {
                session.sendMessage(new TextMessage(speaking.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}