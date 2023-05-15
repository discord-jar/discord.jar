package com.seailz.discordjar.gateway;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.gateway.events.DispatchedEvents;
import com.seailz.discordjar.gateway.events.GatewayEvents;
import com.seailz.discordjar.gateway.heartbeat.HeartbeatManager;
import com.seailz.discordjar.model.application.Intent;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.status.Status;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
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
import java.util.UUID;
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

    private WebSocketSession session;
    private WebSocketClient client;
    public static int sequence;

    private Status status;
    private final String gatewayUrl;
    private String sessionId;
    private String resumeUrl;
    private HeartbeatManager heartbeatManager;
    private boolean shouldResume = false;
    private boolean readyForMessages = false;
    public HashMap<String, GatewayFactory.MemberChunkStorageWrapper> memberRequestChunks = new HashMap<>();
    private final boolean debug;
    public UUID uuid = UUID.randomUUID();

    public GatewayFactory(DiscordJar discordJar, boolean debug) throws ExecutionException, InterruptedException {
        this.discordJar = discordJar;
        this.debug = debug;

        discordJar.setGatewayFactory(this);

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
        connect();

        if (discordJar.getStatus() != null) {
            JSONObject json = new JSONObject();
            json.put("d", discordJar.getStatus().compile());
            json.put("op", 3);
            queueMessage(json);
        }
    }

    public void connect(String customUrl) throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        this.client = client;
        this.session = client.execute(this, new WebSocketHttpHeaders(), URI.create(customUrl + "?v=" + URLS.version.getCode())).get();
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Gateway connection established.");
        }
        discordJar.setGatewayFactory(this);
    }

    public void connect() throws ExecutionException, InterruptedException {
        connect(gatewayUrl);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException, ExecutionException, InterruptedException {
        // connection closed
        logger.info("[DISCORD.JAR] Gateway connection closed. Close code " + status.getCode() + ". Identifying issue...");
        if (this.heartbeatManager != null) heartbeatManager.deactivate();
        heartbeatManager = null;
        logger.info("[DISCORD.JAR] Heartbeat manager deactivated.");

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Gateway connection closed. Status: " + status.getReason() + " (" + status.getCode() + ")");
        }
        switch (status.getCode()) {
            case 1012:
                reconnect();
                break;
            case 1011:
                break;
            case 4000:
                logger.info("[DISCORD.JAR] Gateway connection closed due to an unknown error. It's possible this could be a discord.jar bug, but is unlikely. Will attempt reconnect.");
                reconnect();
                break;
            case 4001:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to an unknown opcode. Will attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                reconnect();
                break;
            case 4002:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to a decode error. Will attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                reconnect();
                break;
            case 4003:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to not being authenticated. Will attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                reconnect();
                break;
            case 4004:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to authentication failure. Will not attempt reconnect. Check your bot token!");
                break;
            case 4005:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to an already authenticated connection. Will attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                reconnect();
                break;
            case 4007:
                logger.info("[DISCORD.JAR] Gateway connection closed due to an invalid sequence. Will attempt reconnect.");
                reconnect();
                break;
            case 4008:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to rate limiting. Will attempt reconnect. Make sure you're not spamming gateway requests!");
                reconnect();
                break;
            case 4009:
                logger.info("[DISCORD.JAR] Gateway connection closed due to an invalid session. Will attempt reconnect. This is nothing to worry about.");
                reconnect();
                break;
            case 4010:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to an invalid shard. Will not attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                break;
            case 4011:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to a shard being required. Will not attempt reconnect. If your bot is in more than 2500 servers, you must use sharding. See discord.jar's GitHub for more info.");
                break;
            case 4012:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to an invalid API version. Will not attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                break;
            case 4013:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to an invalid intent. Will not attempt reconnect. This is usually a bug, please report it on discord.jar's GitHub.");
                break;
            case 4014:
                logger.warning("[DISCORD.JAR] Gateway connection closed due to a disallowed intent. Will not attempt reconnect. If you've set intents, please make sure they are enabled in the developer portal and you're approved for them if you run a verified bot.");
                break;
            case 1000:
                if (!session.isOpen()) {
                    logger.info("[DISCORD.JAR] Gateway connection was closed using the close code 1000. The heartbeat cycle has likely fallen out of sync. Will attempt reconnect.");
                    reconnect();
                }
                break;
            case 1001:
                logger.info("[discord.jar] Gateway requested a reconnect (close code 1001), reconnecting...");
                reconnect();
                break;
            case 1006:
                logger.info("[DISCORD.JAR] Gateway connection was closed using the close code 1006. This is usually an error with Spring. Please post this error with the stacktrace below (if there is one) on discord.jar's GitHub. Will attempt reconnect.");
                reconnect();
                break;
            default:
                logger.warning(
                        "[DISCORD.JAR] Gateway connection was closed with an unknown status code. This is usually a bug, please report it on discord.jar's GitHub with this log message. Status code: "
                                + status.getCode() + ". Reason: " + status.getReason() + ". Will attempt reconnect."
                );
                reconnect();
                break;
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
        super.handleTextMessage(session, message);
        JSONObject payload = new JSONObject(message.getPayload());
        if (discordJar.getGateway() != this) {
            logger.info("[DISCORD.JAR] Received message from a gateway that isn't the main gateway. This is usually a bug, please report it on discord.jar's GitHub with this log message. Payload: " + payload.toString());
            return;
        }

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
                logger.info("[discord.jar] Gateway requested a reconnect, reconnecting...");
                reconnect();
                break;
            case INVALID_SESSION:
                logger.info("[discord.jar] Gateway requested a reconnect (invalid session), reconnecting...");
                reconnect();
                break;
            case HEARTBEAT_ACK:
                // Heartbeat was acknowledged, can ignore.
                break;
        }
    }

    private void handleDispatched(JSONObject payload) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, DiscordRequest.UnhandledDiscordAPIErrorException {
        // Handle dispatched events
        // actually dispatch the event
        Class<? extends Event> eventClass = DispatchedEvents.getEventByName(payload.getString("t")).getEvent().apply(payload, this, discordJar);
        if (eventClass == null) {
            logger.info("[discord.jar] Unhandled event: " + payload.getString("t") + "\nThis is usually ok, if a new feature has recently been added to Discord as discord.jar may not support it yet.\nIf that is not the case, please report this to the discord.jar developers.");
            return;
        }
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Event class: " + eventClass.getName());
        }
        if (eventClass.equals(CommandInteractionEvent.class)) return;

        Event event = eventClass.getConstructor(DiscordJar.class, long.class, JSONObject.class)
                .newInstance(discordJar, sequence, payload);

        discordJar.getEventDispatcher().dispatchEvent(event, eventClass, discordJar);

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Event dispatched: " + eventClass.getName());
        }

        switch (DispatchedEvents.getEventByName(payload.getString("t"))) {
            case READY:
                this.sessionId = payload.getJSONObject("d").getString("session_id");
                this.resumeUrl = payload.getJSONObject("d").getString("resume_gateway_url");
                readyForMessages = true;
                break;
            case GUILD_CREATE:
                discordJar.getGuildCache().cache(Guild.decompile(payload.getJSONObject("d"), discordJar));
                break;
            case RESUMED:
                logger.info("[discord.jar] Gateway session has been resumed, confirmed by Discord.");
                break;
        }
    }

    public void killConnectionNicely() throws IOException {
        // disable heartbeat
        if (heartbeatManager != null) heartbeatManager.deactivate();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        session.close(CloseStatus.GOING_AWAY);
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Connection closed nicely.");
        }
    }

    public void killConnection() throws IOException {
        // disable heartbeat
        if (this.heartbeatManager != null) heartbeatManager.deactivate();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        if (session != null) session.close(CloseStatus.SERVER_ERROR);

        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Connection closed.");
        }
    }

    public void reconnect() throws IOException {
        if (debug) {
            logger.info("[DISCORD.JAR - DEBUG] Attempting resume...");
        }
        if (session.isOpen())
            session.close(CloseStatus.SERVER_ERROR);

        if (this.heartbeatManager != null) this.heartbeatManager.deactivate();
        this.heartbeatManager = null;
        readyForMessages = false;

        // Invalidate this class
        this.discordJar.restartGateway();

        /*JSONObject resumePayload = new JSONObject();
        resumePayload.put("op", 6);
        JSONObject resumeData = new JSONObject();
        resumeData.put("token", discordJar.getToken());
        resumeData.put("session_id", sessionId);
        resumeData.put("seq", sequence);
        resumePayload.put("d", resumeData);
        shouldResume = true;
        connect(resumeUrl); */
    }

    private void handleHello(JSONObject payload) {
        heartbeatManager = new HeartbeatManager(payload.getJSONObject("d").getInt("heartbeat_interval"), this);
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
        String os = System.getProperty("os.name").toLowerCase();
        data.put("properties", new JSONObject().put("os", os).put("browser", "discord.jar").put("device", "discord.jar"));
        data.put("intents", intents.get());
        payload.put("d", data);
        sendPayload(payload);
    }

    public void sendPayload(JSONObject payload) {
        try {
            session.sendMessage(new TextMessage(payload.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return session;
    }

    public boolean isDebug() {
        return debug;
    }
}
