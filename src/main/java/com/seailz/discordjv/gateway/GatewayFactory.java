package com.seailz.discordjv.gateway;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.gateway.events.DispatchedEvents;
import com.seailz.discordjv.gateway.events.GatewayEvents;
import com.seailz.discordjv.gateway.heartbeat.HeartbeatCycle;
import com.seailz.discordjv.model.application.Intent;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.utils.URLS;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class GatewayFactory extends TextWebSocketHandler {

    private DiscordJv discordJv;
    private String url;
    private WebSocketSession clientSession;
    private static int lastSequence;
    public HeartbeatCycle heartbeatCycle;
    private Logger logger;
    private String resumeUrl;
    private String sessionId;
    private final List<JSONObject> queue = new ArrayList<>();
    private boolean ready;


    public GatewayFactory(DiscordJv discordJv) throws ExecutionException, InterruptedException {
        new Thread(() -> {
            this.discordJv = discordJv;
            DiscordResponse response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    "/gateway",
                    discordJv,
                    "/gateway", RequestMethod.GET
            ).invoke();
            this.url = response.body().getString("url");
            logger = Logger.getLogger("DISCORD.JV");
            try {
                initiateConnection();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("[DISCORD.JV] Connected to gateway");
        }, "Gateway").start();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(status.getReason());
    }

    /**
     * Queues a message to be sent to the gateway
     *
     * @param obj The message to be sent
     */
    public void queueMessage(JSONObject obj) throws IOException {
        if (ready) {
            clientSession.sendMessage(new TextMessage(obj.toString()));
            return;
        }

        queue.add(obj);
    }

    public void startAgain() throws ExecutionException, InterruptedException {
        new Thread(() -> {
            this.heartbeatCycle = null;
            DiscordResponse response = new DiscordRequest(
                    new JSONObject(),
                    new HashMap<>(),
                    "/gateway",
                    discordJv,
                    "/gateway", RequestMethod.GET
            ).invoke();
            this.url = response.body().getString("url");
            logger = Logger.getLogger("DISCORD.JV");
            try {
                initiateConnection();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            logger.info("[DISCORD.JV] Connected to gateway");
        }, "Gateway").start();
    }

    private void initiateConnection() throws ExecutionException, InterruptedException {
        // connect to websocket
        WebSocketClient client = new StandardWebSocketClient();
        this.clientSession = client.execute(this, new WebSocketHttpHeaders(), URI.create(url + "?v=" + URLS.version.getCode())).get();
        clientSession.setTextMessageSizeLimit(1000000);
        clientSession.setBinaryMessageSizeLimit(1000000);
    }

    private void initiateConnection(String customURL) throws ExecutionException, InterruptedException {
        // connect to websocket
        WebSocketClient client = new StandardWebSocketClient();
        this.clientSession = client.execute(this, new WebSocketHttpHeaders(), URI.create(customURL + "?v=" + URLS.version.getCode())).get();
        clientSession.setTextMessageSizeLimit(1000000);
        clientSession.setBinaryMessageSizeLimit(1000000);
    }

    public void reconnect() throws ExecutionException, InterruptedException, IOException {
        logger.info("[DISCORD.JV] Reconnecting to gateway");
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
            try {
                initiateConnection(resumeUrl == null ? url : resumeUrl);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            JSONObject payload = new JSONObject();
            payload.put("op", 6);
            payload.put("d", new JSONObject().put("token", discordJv.getToken()).put("session_id", sessionId).put("seq", lastSequence));
            try {
                clientSession.sendMessage(new TextMessage(payload.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }


            logger.info("[DISCORD.JV] Attempting Gateway Resume");
        }).start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JSONObject payload = new JSONObject(message.getPayload());

        try {
            lastSequence = payload.getInt("s");
        } catch (JSONException ignored) {
        }

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
                ready = false;
                break;
            case INVALID_SESSION:
                logger.info("[DISCORD.JV] Gateway requested a reconnect (invalid session), reconnecting...");
                initiateConnection();
                ready = false;
                break;
            case HEARTBEAT_ACK:
                logger.info("[DISCORD.JV] Heartbeat acknowledged");
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Logger logger = Logger.getLogger("DISCORD.JV");
        logger.info("[DISCORD.JV] Gateway connection closed, reconnecting...");
        logger.info("Was disconnected with status [" + status.getCode() + "]" + " " + status.getReason());
    }

    private void handleHello(JSONObject payload) throws InterruptedException {
        // Start heartbeat cycle
        this.heartbeatCycle = new HeartbeatCycle(payload.getJSONObject("d").getInt("heartbeat_interval"), this);
        try {
            heartbeatCycle.sendHeartbeat();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleDispatched(JSONObject payload) throws ExecutionException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Handle dispatched events
        // actually dispatch the event
        Class<? extends Event> eventClass = DispatchedEvents.getEventByName(payload.getString("t")).getEvent().apply(payload, discordJv);
        if (eventClass == null) {
            logger.info("[DISCORD.JV] Unhandled event: " + payload.getString("t"));
            logger.info("This is usually ok, if a new feature has recently been added to Discord as discord.jv may not support it yet.");
            logger.info("If that is not the case, please report this to the discord.jv developers.");
            return;
        }
        if (eventClass.equals(CommandInteractionEvent.class)) return;

        Event event = eventClass.getConstructor(DiscordJv.class, long.class, JSONObject.class).newInstance(discordJv, lastSequence, payload);
        discordJv.getEventDispatcher().dispatchEvent(event, eventClass, discordJv);

        switch (DispatchedEvents.getEventByName(payload.getString("t"))) {
            case READY:
                this.sessionId = payload.getJSONObject("d").getString("session_id");
                this.resumeUrl = payload.getJSONObject("d").getString("resume_gateway_url");

                ready = true;

                queue.forEach(obj -> {
                    try {
                        clientSession.sendMessage(new TextMessage(obj.toString()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                queue.clear();
                break;
            case GUILD_CREATE:
                discordJv.getGuildCache().cache(Guild.decompile(payload.getJSONObject("d"), discordJv));
                break;
            case RESUMED:
                logger.info("[DISCORD.JV] Gateway session has been resumed, confirmed by Discord.");
                break;
        }
    }

    private void sendIdentify() throws IOException {
        AtomicInteger intents = new AtomicInteger();
        if (discordJv.getIntents().contains(Intent.ALL))
            intents.set(3243773);
        else {
            discordJv.getIntents().forEach(intent -> {
                intents.getAndAdd(intent.getLeftShiftId());
            });
        }

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

    /**
     * Closes the connection to the gateway
     */
    public void close() {
        try {
            clientSession.close(CloseStatus.GOING_AWAY);
            logger.info("[DISCORD.JV] Disconnected from gateway. Ready for shutdown.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLastSequence() {
        return lastSequence;
    }

    public WebSocketSession getClientSession() {
        return clientSession;
    }
}
