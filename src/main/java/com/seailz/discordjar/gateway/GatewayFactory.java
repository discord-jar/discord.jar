package com.seailz.discordjar.gateway;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.gateway.events.DispatchedEvents;
import com.seailz.discordjar.gateway.events.GatewayEvents;
import com.seailz.discordjar.model.application.Intent;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.status.Status;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.ws.ExponentialBackoffLogic;
import com.seailz.discordjar.gateway.heartbeat.HeartLogic;
import com.seailz.discordjar.ws.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * An implementation of a client for the Discord gateway.
 * The gateway is how bots receive events, and allows bots to communicate their status to Discord.
 *
 * @author Seailz
 * @since 1.0
 */
public class GatewayFactory extends TextWebSocketHandler {

    private final DiscordJar discordJar;
    private final Logger logger = Logger.getLogger(GatewayFactory.class.getName().toUpperCase());
    public static int sequence;

    private Status status;
    private String gatewayUrl = null;
    private String sessionId;
    private static String resumeUrl;
    private HeartLogic heartbeatManager;
    private boolean shouldResume = false;
    private boolean readyForMessages = false;
    public HashMap<String, GatewayFactory.MemberChunkStorageWrapper> memberRequestChunks = new HashMap<>();
    private final boolean debug;
    private final boolean newSystemForMemoryManagement;
    public UUID uuid = UUID.randomUUID();
    private int shardId;
    private int numShards;
    public static List<Long> pingHistoryMs = new ArrayList<>();
    public static Date lastHeartbeatSent = new Date();

    private WebSocket socket;

    public GatewayFactory(DiscordJar discordJar, boolean debug, int shardId, int numShards, boolean newSystemForMemoryManagement, int nsfgmmPercentOfTotalMemory) throws ExecutionException, InterruptedException {
        logger.info("[Gateway] New instance created");
        this.discordJar = discordJar;
        this.debug = debug;
        this.shardId = shardId;
        this.numShards = numShards;
        this.newSystemForMemoryManagement = newSystemForMemoryManagement;

        discordJar.setGatewayFactory(this);

        if (resumeUrl == null) {
            try {
                DiscordResponse response = null;
                try {
                    response = new DiscordRequest(
                            new JSONObject(),
                            new HashMap<>(),
                            "/gateway",
                            discordJar,
                            "/gateway", RequestMethod.GET
                    ).invoke();
                } catch (DiscordRequest.UnhandledDiscordAPIErrorException ignored) {}
                if (response == null || response.body() == null || !response.body().has("url")) {
                    // In case the request fails, we can attempt to use the backup gateway URL instead.
                    this.gatewayUrl = URLS.GATEWAY.BASE_URL;
                } else this.gatewayUrl = response.body().getString("url");
            } catch (Exception e) {
                logger.warning("[DISCORD.JAR] Failed to get gateway URL. Restarting gateway after 5 seconds.");
                Thread.sleep(5000);
                discordJar.restartGateway();
                return;
            }
        } else gatewayUrl = resumeUrl;

        socket = new WebSocket(gatewayUrl, newSystemForMemoryManagement, debug, nsfgmmPercentOfTotalMemory);

        ExponentialBackoffLogic backoffReconnectLogic = new ExponentialBackoffLogic();
        socket.setReEstablishConnection(backoffReconnectLogic.getFunction());
        backoffReconnectLogic.setAttemptReconnect((c) -> {
            new Thread(discordJar::clearMemberCaches).start();
            return !shouldResume;
        });

        socket.addMessageConsumer((tm) -> {
            try {
                handleTextMessage(socket.getSession(), tm);
            } catch (Exception e) {
                logger.warning("[Gateway] Failed to handle text message: " + e.getMessage());
            }
        });

        socket.connect().onSuccess(this::onConnect).onFailed((e) -> {
            logger.severe("[Gateway] Failed to establish connection: " + e.getThrowable().getMessage() + " - Will attempt to reconnect after 5 seconds.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            discordJar.restartGateway();
        });

        socket.addOnConnect(() -> {
            onConnect(null);
        });

        socket.addOnDisconnectConsumer((cs) -> {
            this.heartbeatManager = null;
            readyForMessages = false;
            attemptReconnect(socket.getSession(), cs);
            discordJar.clearMemberCaches();
        });
    }

    private void onConnect(Void vd) {
        if (ifNotSelf()) return;
        logger.info("[Gateway] Connection established successfully. ⚡");
    }

    public boolean attemptReconnect(WebSocketSession session, CloseStatus status) {
        // connection closed
        if (this.heartbeatManager != null) heartbeatManager.stop();
        heartbeatManager = null;

        logger.info("[GW] [" + status.getCode() + "] " + status.getReason());

        switch (status.getCode()) {
            case 1012:
                return true;
            case 1011, 1015:
                return false;
            case 4000:
                if (debug) logger.info("[Gateway] Connection was closed due to an unknown error. Will attempt reconnect.");
                return true;
            case 4001:
                if (debug) logger.warning("[Gateway] Connection was closed due to an unknown opcode. Will attempt reconnect. This is usually a discord.jar bug.");
                return true;
            case 4002:
                if (debug) logger.warning("[Gateway] Connection was closed due to a decode error. Will attempt reconnect.");
                return true;
            case 4003:
                if (debug) logger.warning("[Gateway] Connection was closed due to not being authenticated. Will attempt reconnect.");
                return true;
            case 4004:
                if (debug) logger.warning("[Gateway] Connection was closed due to authentication failure. Will not attempt reconnect. Check your bot token!");
                return false;
            case 4005:
                if (debug) logger.warning("[Gateway] Connection was closed due to an already authenticated connection. Will attempt reconnect.");
                return true;
            case 4007:
                if (debug) logger.info("[Gateway] Connection was closed due to an invalid sequence. Will attempt reconnect.");
                return true;
            case 4008:
                if (debug) logger.warning("[Gateway] Connection was closed due to rate limiting. Will attempt reconnect. Make sure you're not spamming gateway requests!");
                return true;
            case 4009:
                if (debug) logger.info("[Gateway] Connection was closed due to an invalid session. Will attempt reconnect. This is nothing to worry about.");
                return true;
            case 4010:
                if (debug) logger.warning("[Gateway] Connection was closed due to an invalid shard. Will not attempt reconnect.");
                return false;
            case 4011:
                if (debug) logger.warning("[Gateway] Connection was closed due to a shard being required. Will not attempt reconnect. If your bot is in more than 2500 servers, you must use sharding. See discord.jar's GitHub for more info.");
                return false;
            case 4012:
                if (debug) logger.warning("[Gateway] Connection was closed due to an invalid API version. Will not attempt reconnect.");
                return false;
            case 4013:
                if (debug) logger.warning("[Gateway] Connection was closed due to an invalid intent. Will not attempt reconnect.");
                return false;
            case 4014:
                if (debug)  logger.warning("[Gateway] Connection was closed due to a disallowed intent. Will not attempt reconnect. If you've set intents, please make sure they are enabled in the developer portal and you're approved for them if you run a verified bot.");
                return false;
            case 1000:
                if (debug) logger.info("[Gateway] Connection was closed using the close code 1000. The heartbeat cycle has likely fallen out of sync. Will attempt reconnect.");
                return true;
            case 1001:
                if (debug) logger.info("[Gateway] Gateway requested a reconnect (close code 1001), reconnecting...");
                return true;
            case 1006:
                if (debug) logger.info("[Gateway] Connection was closed using the close code 1006. This is usually an error with Spring. Please post this error with the stacktrace below (if there is one) on discord.jar's GitHub. Will attempt reconnect.");
                discordJar.restartGateway();
                return false;
            default:
                logger.warning(
                        "[Gateway] Connection was closed with an unknown status code. Status code: "
                                + status.getCode() + ". Reason: " + status.getReason() + ". Will attempt reconnect."
                );
                return true;
        }
    }


    /**
     * Do not use this method - it is for internal use only.
     * @param status The status to set.
     */
    @Deprecated
    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void queueMessage(JSONObject payload) {
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Queued message: " + payload.toString());
        }
        while (readyForMessages) {
            sendPayload(payload);
            if (debug) {
                logger.info("[DISCORD.JAR - DEBUG] Sent message: " + payload.toString());
            }
            break;
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (ifNotSelf()) return;
        super.handleTextMessage(session, message);
        JSONObject payload = new JSONObject(message.getPayload());

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Received message: " + payload.toString());
        }
        try {
            sequence = payload.getInt("s");
        } catch (JSONException ignored) {
        }

        GatewayEvents event = GatewayEvents.getEvent(payload.getInt("op"));
        if (event == null) {
            logger.warning("[discord.jar] Unknown event received: " + payload.getInt("op") + ". This is rare, please create an issue on GitHub with this log message. Payload: " + payload.toString());
            return;
        }

        switch (event) {
            case HELLO:
                handleHello(payload);
                if (!shouldResume) sendIdentify();
                this.shouldResume = false;
                readyForMessages = true;

                if (debug) {
                    logger.info("[DISCORD.JAR - DEBUG] Received HELLO event. Heartbeat cycle has been started. If this isn't a resume, IDENTIFY has been sent.");
                }
                break;
            case HEARTBEAT_REQUEST:
                heartbeatManager.forceHeartbeat();
                if (debug) {
                    logger.info("[DISCORD.JAR - DEBUG] Received HEARTBEAT_REQUEST event. Heartbeat has been forced.");
                }
                break;
            case DISPATCHED:
                handleDispatched(payload);
                if (debug) {
                    logger.info("[DISCORD.JAR - DEBUG] Received DISPATCHED event. Event has been handled.");
                }
                break;
            case RECONNECT:
                logger.info("[discord.jar] Gateway requested a reconnect, reconnecting after 5 seconds...");
                Thread.sleep(5000);
                reconnect();
                break;
            case INVALID_SESSION:
                logger.info("[discord.jar] Gateway requested a reconnect (invalid session), reconnecting after 5 seconds...");
                Thread.sleep(5000);
                reconnect();
                break;
            case HEARTBEAT_ACK:
                // Heartbeat was acknowledged, can ignore, but we'll log the request ping anyway.
                if (lastHeartbeatSent != null) {
                    long ping = System.currentTimeMillis() - lastHeartbeatSent.getTime();
                    if (debug) {
                        logger.info("[DISCORD.JAR - DEBUG] Received HEARTBEAT_ACK event. Ping: " + ping + "ms");
                    }

                    pingHistoryMs.add(ping);
                }
                break;
        }
    }

    private boolean ifNotSelf() {
//        if (!discordJar.getGateway().equals(this)) {
//            logger.warning("[Gateway] Not the current gateway instance. Shutting down.");
//            shouldResume = true; // Stop reconnecting
//            try {
//                socket.getSession().close(CloseStatus.SERVER_ERROR);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return true;
//        }
        return false;
    }

    private void handleDispatched(JSONObject payload) {
        // Handle dispatched events
        // actually dispatch the event
        Class<? extends Event> eventClass = DispatchedEvents.getEventByName(payload.getString("t")).getEvent().apply(payload, this, discordJar);
        if (eventClass == null) {
            if (debug) logger.info("[discord.jar] Unhandled event: " + payload.getString("t") + "\nThis is usually ok, if a new feature has recently been added to Discord as discord.jar may not support it yet.\nIf that is not the case, please report this to the discord.jar developers.");
            return;
        }
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Event class: " + eventClass.getName());
        }
        if (eventClass.equals(CommandInteractionEvent.class)) return;

        new Thread(() -> {
            Event event;
            try {
                event = eventClass.getConstructor(DiscordJar.class, long.class, JSONObject.class)
                        .newInstance(discordJar, sequence, payload);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                logger.warning("[DISCORD.JAR - EVENTS] Failed to dispatch " + eventClass.getName() + " event. This is usually a bug, please report it on discord.jar's GitHub with this log message.");
                e.printStackTrace();
                return;
            } catch (InvocationTargetException e) {
                logger.warning("[DISCORD.JAR - EVENTS] Failed to dispatch " + eventClass.getName() + " event. This is usually a bug, please report it on discord.jar's GitHub with this log message.");
                // If it's a runtime exception, we want to catch it and print the stack trace.
                e.getCause().printStackTrace();
                return;
            }

            discordJar.getEventDispatcher().dispatchEvent(event, eventClass, discordJar);

            if (debug) {
                logger.info("[DISCORD.JAR - DEBUG] Event dispatched: " + eventClass.getName());
            }
        }).start();

        if (Objects.requireNonNull(DispatchedEvents.getEventByName(payload.getString("t"))) == DispatchedEvents.READY) {
            this.sessionId = payload.getJSONObject("d").getString("session_id");
            this.resumeUrl = payload.getJSONObject("d").getString("resume_gateway_url");
            readyForMessages = true;

            if (discordJar.getStatus() != null) {
                JSONObject json = new JSONObject();
                json.put("d", discordJar.getStatus().compile());
                json.put("op", 3);
                queueMessage(json);
            }
        }
    }

    public void killConnectionNicely() throws IOException {
        // disable heartbeat
        if (heartbeatManager != null) heartbeatManager.stop();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        getSocket().getSession().close(CloseStatus.GOING_AWAY);
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Connection closed nicely.");
        }
    }

    public void killConnection() throws IOException {
        // disable heartbeat
        if (this.heartbeatManager != null) heartbeatManager.stop();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        if (getSocket() != null && getSocket().getSession() != null) getSocket().getSession().close(CloseStatus.TLS_HANDSHAKE_FAILURE);

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Connection closed.");
        }
    }

    public void reconnect() throws IOException {
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Attempting resume...");
        }
        if (getSocket().getSession().isOpen())
            getSocket().getSession().close(CloseStatus.SERVER_ERROR);

//        socket.connect();
//        onConnect(null);
//
//        JSONObject resumePayload = new JSONObject();
//        resumePayload.put("op", 6);
//        JSONObject resumeData = new JSONObject();
//        resumeData.put("token", discordJar.getToken());
//        resumeData.put("session_id", sessionId);
//        resumeData.put("seq", sequence);
//        resumePayload.put("d", resumeData);
        //queueMessage(resumePayload);
    }

    private void handleHello(JSONObject payload) {
        heartbeatManager = new HeartLogic(socket, payload.getJSONObject("d").getInt("heartbeat_interval"));
        heartbeatManager.start();
    }

    private void sendIdentify() {
        AtomicInteger intents = new AtomicInteger();
        if (discordJar.getIntents().contains(Intent.ALL)) {
            intents.set(3243773);
            discordJar.getIntents().forEach(intent -> {
                if (intent.isPrivileged()) {
                    intents.getAndAdd(intent.getLeftShiftId());
                }
            });
        } else {
            discordJar.getIntents().forEach(intent -> intents.getAndAdd(intent.getLeftShiftId()));
        }

        JSONObject payload = new JSONObject();
        payload.put("op", 2);
        JSONObject data = new JSONObject();
        data.put("token", discordJar.getToken());
        if (numShards != -1 && shardId != -1) {
            data.put("shard", new JSONArray().put(shardId).put(numShards));
        }
        String os = System.getProperty("os.name").toLowerCase();
        data.put("properties", new JSONObject().put("os", os).put("browser", "discord.jar").put("device", "discord.jar"));
        data.put("intents", intents.get());
        payload.put("d", data);
        sendPayload(payload);
    }

    public void sendPayload(JSONObject payload) {
        try {
            getSocket().send(payload.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WebSocket getSocket() {
        return socket;
    }

    public void requestGuildMembers(RequestGuildMembersAction action, CompletableFuture<List<Member>> future) {
        if (action.getQuery() == null & action.getUserIds() == null) {
            throw new IllegalArgumentException("You must provide either a query or a list of user ids");
        }

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Requesting guild members...");
        }

        JSONObject payload = new JSONObject();
        JSONObject dPayload = new JSONObject();
        dPayload.put("guild_id", action.getGuildId());
        if (action.getQuery() != null) {
            dPayload.put("query", action.getQuery());
        }

        if (action.getUserIds() != null && !action.getUserIds().isEmpty()) {
            dPayload.put("user_ids", action.getUserIds());
        }

        dPayload.put("limit", action.getLimit());
        if (action.isPresences()) dPayload.put("presences", true);
        dPayload.put("nonce", action.getNonce());
        payload.put("op", 8);
        payload.put("d", dPayload);

        queueMessage(payload);

        memberRequestChunks.put(action.getNonce(), new GatewayFactory.MemberChunkStorageWrapper(new ArrayList<>(), future));
    }

    public record MemberChunkStorageWrapper(List<Member> members, CompletableFuture<List<Member>> future) {
        public void addMember(Member member) {
            members.add(member);
        }

        public void complete() {
            future.complete(members);
        }
    }


    public WebSocketSession getSession() {
        return getSocket().getSession();
    }

    public boolean isDebug() {
        return debug;
    }
}
