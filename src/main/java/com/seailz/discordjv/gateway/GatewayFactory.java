package com.seailz.discordjv.gateway;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjv.events.model.Event;
import com.seailz.discordjv.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjv.gateway.events.DispatchedEvents;
import com.seailz.discordjv.gateway.events.GatewayEvents;
import com.seailz.discordjv.gateway.heartbeat.HeartbeatManager;
import com.seailz.discordjv.model.application.Intent;
import com.seailz.discordjv.model.guild.Guild;
import com.seailz.discordjv.model.guild.Member;
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

    private final DiscordJv discordJar;
    private final Logger logger = Logger.getLogger(GatewayFactory.class.getName().toUpperCase());

    private WebSocketSession session;
    private WebSocketClient client;
    public static int sequence;

    private final String gatewayUrl;
    private String sessionId;
    private String resumeUrl;
    private HeartbeatManager heartbeatManager;
    private boolean shouldResume = false;
    private boolean readyForMessages = false;
    public HashMap<String, GatewayFactory.MemberChunkStorageWrapper> memberRequestChunks;

    public GatewayFactory(DiscordJv discordJar) throws ExecutionException, InterruptedException {
        this.discordJar = discordJar;
        DiscordResponse response = new DiscordRequest(
                new JSONObject(),
                new HashMap<>(),
                "/gateway",
                discordJar,
                "/gateway", RequestMethod.GET
        ).invoke();
        this.gatewayUrl = response.body().getString("url");
        connect();
    }

    public void connect(String customUrl) throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();
        this.client = client;
        this.session = client.execute(this, new WebSocketHttpHeaders(), URI.create(customUrl + "?v=" + URLS.version.getCode())).get();
        session.setTextMessageSizeLimit(1000000);
        session.setBinaryMessageSizeLimit(1000000);
    }

    public void connect() throws ExecutionException, InterruptedException {
        connect(gatewayUrl);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException, ExecutionException, InterruptedException {
        // connection closed
        logger.info("[DISCORD.JAR] Gateway connection closed. Identifying issue...");
        if (this.heartbeatManager != null) heartbeatManager.deactivate();
        heartbeatManager = null;
        logger.info("[DISCORD.JAR] Heartbeat manager deactivated.");

        switch (status.getCode()) {
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
            default:
                logger.warning(
                        "[DISCORD.JAR] Gateway connection was closed with an unknown status code. This is usually a bug, please report it on discord.jar's GitHub with this log message. Status code: "
                                + status.getCode() + ". Reason: " + status.getReason() + ". Will not attempt reconnect."
                );
                break;
        }
    }

    public void queueMessage(JSONObject payload) {
        while (readyForMessages) {
            sendPayload(payload);
            break;
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        JSONObject payload = new JSONObject(message.getPayload());

        try {
            sequence = payload.getInt("s");
        } catch (JSONException ignored) {
        }

        GatewayEvents event = GatewayEvents.getEvent(payload.getInt("op"));
        if (event == null) {
            logger.warning("[DISCORD.JV] Unknown event received: " + payload.getInt("op") + ". This is rare, please create an issue on GitHub with this log message. Payload: " + payload.toString());
            return;
        }

        switch (event) {
            case HELLO:
                handleHello(payload);
                if (!shouldResume) sendIdentify();
                readyForMessages = true;
                break;
            case HEARTBEAT_REQUEST:
                heartbeatManager.forceHeartbeat();
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
                if (payload.getBoolean("d")) {
                    reconnect();
                } else {
                    sendIdentify();
                }

                session.close(CloseStatus.GOING_AWAY);
                heartbeatManager.deactivate();
                readyForMessages = false;
                heartbeatManager = null;
                connect();
                break;
            case HEARTBEAT_ACK:
                // Heartbeat was acknowledged, can ignore.
                break;
        }
    }

    private void handleDispatched(JSONObject payload) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Handle dispatched events
        // actually dispatch the event
        Class<? extends Event> eventClass = DispatchedEvents.getEventByName(payload.getString("t")).getEvent().apply(payload, this, discordJar);
        if (eventClass == null) {
            logger.info("[DISCORD.JV] Unhandled event: " + payload.getString("t") + "\nThis is usually ok, if a new feature has recently been added to Discord as discord.jar may not support it yet.\nIf that is not the case, please report this to the discord.jar developers.");
            return;
        }
        if (eventClass.equals(CommandInteractionEvent.class)) return;

        Event event = eventClass.getConstructor(DiscordJv.class, long.class, JSONObject.class)
                .newInstance(discordJar, sequence, payload);

        discordJar.getEventDispatcher().dispatchEvent(event, eventClass, discordJar);

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
                logger.info("[DISCORD.JV] Gateway session has been resumed, confirmed by Discord.");
                break;
        }
    }

    public void killConnectionNicely() throws IOException {
        // disable heartbeat
        heartbeatManager.deactivate();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        session.close(CloseStatus.GOING_AWAY);
    }

    public void killConnection() throws IOException {
        // disable heartbeat
        heartbeatManager.deactivate();
        heartbeatManager = null;
        readyForMessages = false;
        // close connection
        session.close(CloseStatus.SERVER_ERROR);
    }

    public void reconnect() throws IOException, ExecutionException, InterruptedException {
        if (session.isOpen())
            session.close(CloseStatus.SERVICE_RESTARTED);

        if (this.heartbeatManager != null) this.heartbeatManager.deactivate();
        this.heartbeatManager = null;
        readyForMessages = false;

        JSONObject resumePayload = new JSONObject();
        resumePayload.put("op", 6);
        JSONObject resumeData = new JSONObject();
        resumeData.put("token", discordJar.getToken());
        resumeData.put("session_id", sessionId);
        resumeData.put("seq", sequence);
        resumePayload.put("d", resumeData);
        shouldResume = true;
        connect(resumeUrl);

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

}
