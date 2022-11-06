package com.seailz.javadiscordwrapper.gateway;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.gateway.events.DispatchedEvents;
import com.seailz.javadiscordwrapper.gateway.events.GatewayEvents;
import com.seailz.javadiscordwrapper.gateway.heartbeat.HeartbeatCycle;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.utils.Requester;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class GatewayFactory extends TextWebSocketHandler {

    private DiscordJv discordJv;
    private String url;
    private WebSocketSession clientSession;
    private int lastSequence;
    private HeartbeatCycle heartbeatCycle;
    private Logger logger;
    private String resumeUrl;
    private String sessionId;


    public GatewayFactory(DiscordJv discordJv) throws ExecutionException, InterruptedException {
        this.discordJv = discordJv;
        this.url = Requester.get("/gateway", discordJv).getString("url");
        logger = Logger.getLogger("DISCORD.JV");
        initiateConnection();
        logger.info("[DISCORD.JV] Connected to gateway");
    }

    private void initiateConnection() throws ExecutionException, InterruptedException {
        // connect to websocket
        WebSocketClient client = new StandardWebSocketClient();
        this.clientSession = client.doHandshake(this, new WebSocketHttpHeaders(), URI.create(url)).get();
    }

    private void initiateConnection(String customURL) throws ExecutionException, InterruptedException {
        // connect to websocket
        WebSocketClient client = new StandardWebSocketClient();
        this.clientSession = client.doHandshake(this, new WebSocketHttpHeaders(), URI.create(customURL)).get();
    }

    public void reconnect() throws ExecutionException, InterruptedException, IOException {
        try {
            clientSession.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            logger.info("[DISCORD.JV] Client was disconnected from gateway, reconnecting...");
            try {
                initiateConnection(resumeUrl);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            JSONObject payload = new JSONObject();
            payload.put("op", 6);
            payload.put("d", new JSONObject().put("token", discordJv.getToken()).put("session_id", sessionId).put("seq", lastSequence));
            try {
                clientSession.sendMessage(new TextMessage(payload.toString()));
                System.out.println(payload.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


            logger.info("[DISCORD.JV] Gateway session has been resumed");
        }).start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JSONObject payload = new JSONObject(message.getPayload());

        try {
           lastSequence = payload.getInt("s");
        } catch (JSONException ignored) {}

        System.out.println(payload.getInt("op"));
        switch (GatewayEvents.getEvent(payload.getInt("op"))) {
            case HELLO:
                handleHello(payload);
                sendIdentify();
                break;
            case HEARTBEAT_REQUEST:
                heartbeatCycle.sendHeartbeat();
                break;
            case DISPATCHED:
                handleDispatched(payload);
                break;
            case RECONNECT:
                logger.info("[DISCORD.JV] Gateway requested a reconnect, reconnecting...");
                reconnect();
                break;
            case INVALID_SESSION:
                logger.info("[DISCORD.JV] Gateway requested a reconnect (invalid session), reconnecting...");
                clientSession.close();
                initiateConnection();
                break;
            case HEARTBEAT_ACK:
                logger.info("[DISCORD.JV] Heartbeat acknowledged");
                break;
        }
    }

    private void handleHello(JSONObject payload) {
        // Start heartbeat cycle
        this.heartbeatCycle = new HeartbeatCycle(payload.getJSONObject("d").getInt("heartbeat_interval"), this);
    }

    private void handleDispatched(JSONObject payload) throws IOException, ExecutionException, InterruptedException {
        // Handle dispatched events
        System.out.println(payload.getString("t"));
        switch (DispatchedEvents.getEventByName(payload.getString("t"))) {
            case READY:
                this.sessionId = payload.getJSONObject("d").getString("session_id");
                this.resumeUrl = payload.getJSONObject("d").getString("resume_gateway_url");
                break;
            case GUILD_CREATE:
                discordJv.getGuildCache().add(new Guild());
                break;
            case RESUMED:
                logger.info("[DISCORD.JV] Gateway session has been resumed, confirmed by Discord.");
                break;
            case MESSAGE_CREATE:
                logger.info("[DISCORD.JV] Message received: " + payload.getJSONObject("d").getString("content"));
                if (payload.getJSONObject("d").getString("content").equals("reconnect"))
                    reconnect();
                break;
        }
    }

    private void sendIdentify() throws IOException {
        AtomicInteger intents = new AtomicInteger();
        discordJv.getIntents().forEach(intent -> {
            intents.getAndAdd(intent.getLeftShiftId());
        });

        JSONObject payload = new JSONObject();
        payload.put("op", 2);
        JSONObject data = new JSONObject();
        data.put("token", discordJv.getToken());
        data.put("properties", new JSONObject().put("os", "linux").put("browser", "discord.jv").put("device", "discord.jv"));
        data.put("intents", intents.get());
        payload.put("d", data);
        synchronized (clientSession) {
            clientSession.sendMessage(new TextMessage(payload.toString()));
        }
    }

    public int getLastSequence() {
        return lastSequence;
    }

    public WebSocketSession getClientSession() {
        return clientSession;
    }
}
