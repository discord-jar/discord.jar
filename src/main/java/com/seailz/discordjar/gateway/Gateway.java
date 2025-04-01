package com.seailz.discordjar.gateway;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.action.guild.members.RequestGuildMembersAction;
import com.seailz.discordjar.events.model.Event;
import com.seailz.discordjar.events.model.interaction.command.CommandInteractionEvent;
import com.seailz.discordjar.gateway.events.DispatchedEvents;
import com.seailz.discordjar.gateway.events.GatewayEvents;
import com.seailz.discordjar.gateway.heartbeat.HeartLogic;
import com.seailz.discordjar.model.api.version.APIVersion;
import com.seailz.discordjar.model.application.Intent;
import com.seailz.discordjar.model.guild.Member;
import com.seailz.discordjar.model.status.Status;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.rest.DiscordResponse;
import com.seailz.discordjar.voice.model.VoiceServerUpdate;
import com.seailz.discordjar.voice.model.VoiceState;
import com.seailz.discordjar.ws.ExponentialBackoffLogic;
import com.seailz.discordjar.ws.WebSocket;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.socket.CloseStatus;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * The Gateway is the real-time two-way WebSocket between Discord and the bot.
 * <br>This class is responsible for handling the connection to the Gateway, as well as handling & sending messages.
 *
 * <p>This is the v3 implementation of the Gateway, designed for stability, simplicity, and performance.
 *
 * <p>For more information on the Gateway, see the <a href="https://discord.com/developers/docs/topics/gateway">Discord API docs</a>.
 * @see <a href="https://discord.com/developers/docs/topics/gateway">Discord API docs</a>
 * @see HeartLogic
 * @see DispatchedEvents
 * @see GatewayEvents
 * @see GatewayTransportCompressionType
 * @see DiscordJar
 * @author Seailz
 * @since b-1.1
 */
public class Gateway {

    private final DiscordJar bot;
    private final Logger logger = Logger.getLogger("Gateway");
    private int shardCount;
    private int shardId;
    private GatewayTransportCompressionType compressionType;
    private WebSocket socket;
    private boolean resumedConnection = false;
    private boolean reconnecting = false;
    private ReconnectInfo resumeInfo;
    public long lastSequenceNumber = -1;
    private boolean readyForMessages = false;
    private boolean receivedReady = false;
    private HeartLogic heartbeatManager;
    public Date lastHeartbeatSent = new Date();

    public List<Long> pingHistoryMs = new ArrayList<>();
    /**
     * Allows you to set the maximum size of the ping history. This is initially limited to 100 to avoid memory issues in long-running bots.
     * If the array exceeds the limit, the oldest ping will be removed.
     */
    public static int maxPingHistorySize = 100;

    private final List<Consumer<VoiceState>> onVoiceStateUpdateListeners = new ArrayList<>();
    private final List<Consumer<VoiceServerUpdate>> onVoiceServerUpdateListeners = new ArrayList<>();
    public final HashMap<String, Gateway.MemberChunkStorageWrapper> memberRequestChunks = new HashMap<>();
    private Status status = null;

    protected Gateway(DiscordJar bot, int shardCount, int shardId, GatewayTransportCompressionType compressionType) {
        this.bot = bot;
        this.shardCount = shardCount;
        this.shardId = shardId;
        this.compressionType = compressionType;

        connectionFlow();
    }

    /**
     * Flow for connecting to the Gateway.
     */
    private void connectionFlow() {
        String gatewayUrl;
        try {
            gatewayUrl = getGatewayUrl();
            if (bot.isDebug()) logger.info("[Gateway - Connection Flow] Gateway URL: " + gatewayUrl);
        } catch (InterruptedException e) {
            logger.warning("[Gateway - Connection Flow] Failed to get gateway URL, retrying...");
            connectionFlow();
            return;
        }

        gatewayUrl = appendGatewayQueryParams(gatewayUrl);
        if (bot.isDebug()) logger.info("[Gateway - Connection Flow] Gateway URL with query params: " + gatewayUrl);

        socket = new WebSocket(gatewayUrl, bot.isDebug(), bot);
        setupDisconnectedSocket(socket);
        connectToSocket(socket, false);

        lastSequenceNumber = -1;
    }

    /**
     * Disconnects from the Gateway.
     * @param closeStatus The close status of the disconnect.
     */
    public void disconnect(@NotNull CloseStatus closeStatus) {
        socket.disconnect(closeStatus.getCode(), closeStatus.getReason());
    }

    /**
     * Flow after a disconnect from the Gateway.
     * @param closeStatus The close status of the disconnect.
     */
    public void disconnectFlow(@NotNull CloseStatus closeStatus) {
        setReceivedReady(false);
        heartbeatManager.stop(); // Stop attempting heartbeats to avoid broken pipe errors
        CloseCode closeCode = CloseCode.fromCode(closeStatus.getCode());
        readyForMessages = false;
        boolean attemptReconnect = closeCode.shouldReconnect();
        if (!attemptReconnect) {
            logger.severe("[Gateway] Connection closed, won't attempt reconnect - " + closeCode.getLog());
            return;
        }

        if (closeCode.shouldResume()) resumeFlow();
        else reconnecting = true;

        if (bot.isDebug()) logger.info("[Gateway] Finished disconnect flow.");
    }

    /**
     * Flow for resuming a connection to the Gateway.
     */
    private void resumeFlow() {
        if (resumeInfo == null) {
            logger.warning("[Gateway - Resume Flow] Resume info is null, cannot resume. Attempting normal connection.");
            connectionFlow();
            return;
        }

        String connectUrl = appendGatewayQueryParams(resumeInfo.url());
        if (bot.isDebug()) logger.info("[Gateway - Resume Flow] Resume URL: " + connectUrl);
        socket = new WebSocket(connectUrl, bot.isDebug(), bot);
        setupDisconnectedSocket(socket);
        connectToSocket(socket, true);

        resumedConnection = true;
    }

    /**
     * Handles a message received from the Gateway.
     * @param message The message received from the Gateway.
     */
    protected void handleTextMessage(String message) {
        JSONObject payload = new JSONObject(message);

        if (bot.isDebug()) {
            logger.info("[Gateway - DEBUG] Received message: " + payload.toString());
            logger.info("[Gateway - DEBUG] Message size: " + payload.toString().getBytes(StandardCharsets.UTF_8).length + "b");
        }

        try {
            lastSequenceNumber = payload.getInt("s");
            if (heartbeatManager != null) heartbeatManager.setLastSequence(lastSequenceNumber);
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
                if (!resumedConnection) sendIdentify();
                readyForMessages = true;

                if (bot.isDebug()) {
                    logger.info("[Gateway] Received HELLO event. Heartbeat cycle has been started. If this isn't a resume, IDENTIFY has been sent.");
                }
                break;
            case HEARTBEAT_REQUEST:
                heartbeatManager.forceHeartbeat();
                if (bot.isDebug()) {
                    logger.info("[Gateway] Received HEARTBEAT_REQUEST event. Heartbeat has been forced.");
                }
                break;
            case DISPATCHED:
                handleDispatch(payload);
                if (bot.isDebug()) {
                    logger.info("[Gateway] Received DISPATCHED event. Event has been handled.");
                }
                break;
            case RECONNECT:
                logger.info("[Gateway] Gateway requested a reconnect, reconnecting...");
                disconnect(CloseStatus.SESSION_NOT_RELIABLE);
                break;
            case INVALID_SESSION:
                logger.info("[Gateway] Gateway requested a reconnect (invalid session), reconnecting...");
                disconnect(CloseStatus.SESSION_NOT_RELIABLE);
                break;
            case HEARTBEAT_ACK:
                // Heartbeat was acknowledged, can ignore, but we'll log the request ping anyway.
                if (lastHeartbeatSent != null) {
                    long ping = System.currentTimeMillis() - lastHeartbeatSent.getTime();
                    if (bot.isDebug()) {
                        logger.info("[Gateway] Received HEARTBEAT_ACK event. Ping: " + ping + "ms");
                    }

                    if (pingHistoryMs.size() > maxPingHistorySize) {
                        pingHistoryMs.remove(0);
                    }
                    pingHistoryMs.add(ping);
                }
                break;
        }
    }

    /**
     * Handles a DISPATCHED event.
     * @param payload The payload of the event.
     */
    private void handleDispatch(@NotNull JSONObject payload) {
        // Handle dispatched events
        // actually dispatch the event
        Class<? extends Event> eventClass = DispatchedEvents.getEventByName(payload.getString("t")).getEvent().apply(payload, this, bot);
        if (eventClass == null) {
            if (bot.isDebug()) logger.info("[discord.jar] Unhandled event: " + payload.getString("t") + "\nThis is usually ok, if a new feature has recently been added to Discord as discord.jar may not support it yet.\nIf that is not the case, please report this to the discord.jar developers.");
            eventClass = Event.class;
        }
        if (bot.isDebug()) {
            logger.info("[Gateway] Event class: " + eventClass.getName());
        }
        if (eventClass.equals(CommandInteractionEvent.class)) return;

        Class<? extends Event> finalEventClass = eventClass;
        new Thread(() -> {
            Event event;
            try {
                event = finalEventClass.getConstructor(DiscordJar.class, long.class, JSONObject.class)
                        .newInstance(bot, lastSequenceNumber, payload);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                logger.warning("[Gateway] Failed to dispatch " + finalEventClass.getName() + " event. This is usually a bug, please report it on discord.jar's GitHub with this log message.");
                e.printStackTrace();
                return;
            } catch (InvocationTargetException e) {
                logger.warning("[Gateway] Failed to dispatch " + finalEventClass.getName() + " event. This is usually a bug, please report it on discord.jar's GitHub with this log message.");
                // If it's a runtime exception, we want to catch it and print the stack trace.
                e.getCause().printStackTrace();
                return;
            }

            event.setName(payload.getString("t"));

            bot.getEventDispatcher().dispatchEvent(event, finalEventClass, bot);

            if (bot.isDebug()) {
                logger.info("[Gateway] Event dispatched: " + finalEventClass.getName());
            }
        }, "djar--event-dispatch-gw").start();

        if (Objects.requireNonNull(DispatchedEvents.getEventByName(payload.getString("t"))) == DispatchedEvents.READY) {

            resumeInfo = new ReconnectInfo(
                    payload.getJSONObject("d").getString("session_id"),
                    bot.getToken(),
                    payload.getJSONObject("d").getString("resume_gateway_url")
            );
            readyForMessages = true;

            if (bot.getStatus() != null) {
                JSONObject json = new JSONObject();
                json.put("d", bot.getStatus().compile());
                json.put("op", OpCodes.PRESENCE_UPDATE.opCode);
                queueMessage(json);
            }
        }
    }

    /**
     * Starts the heartbeat cycle.
     * @param payload The HELLO payload.
     */
    private void handleHello(@NotNull JSONObject payload) {
        if (heartbeatManager != null) {
            heartbeatManager.setSocket(socket);
            heartbeatManager.startCycle();
            return;
        }
        heartbeatManager = new HeartLogic(socket, payload.getJSONObject("d").getInt("heartbeat_interval"), this);
        heartbeatManager.start();
    }

    /**
     * Sends an IDENTIFY payload to the gateway.
     */
    private void sendIdentify() {
        AtomicInteger intents = new AtomicInteger();
        if (bot.getIntents().contains(Intent.ALL)) {
            intents.set(3243773);
            bot.getIntents().forEach(intent -> {
                if (intent.isPrivileged()) {
                    intents.getAndAdd(intent.getLeftShiftId());
                }
            });
        } else {
            bot.getIntents().forEach(intent -> intents.getAndAdd(intent.getLeftShiftId()));
        }

        JSONObject payload = new JSONObject();
        payload.put("op", OpCodes.IDENTIFY.opCode);
        JSONObject data = new JSONObject();
        data.put("token", bot.getToken());
        if (shardCount != -1 && shardId != -1) {
            data.put("shard", new JSONArray().put(shardId).put(shardCount));
        }
        String os = System.getProperty("os.name").toLowerCase();
        data.put("properties", new JSONObject().put("os", os).put("browser", "discord.jar").put("device", "discord.jar"));
        data.put("intents", intents.get());
        payload.put("d", data);
        socket.send(payload.toString());
    }

    /**
     * Given a {@link WebSocket} instance, connects to the gateway using it and returns the same {@link WebSocket} instance.
     */
    @Contract("_, _ -> param1")
    private @NotNull WebSocket connectToSocket(@NotNull WebSocket socket, boolean resuming) {
        socket.connect()
                .onFailed((e) -> {
                    logger.warning("[Gateway - Connection Flow] Failed to connect to gateway, retrying...");
                    connectionFlow();
                })
                .onSuccess((v) -> {
                    logger.info("[Gateway] Connection established successfully. ⚡");

                    if (resuming) {
                        JSONObject resumeObject = new JSONObject();
                        resumeObject.put("op", OpCodes.RESUME.opCode);
                        resumeObject.put("d", new JSONObject()
                                .put("token", bot.getToken())
                                .put("session_id", resumeInfo.sessionId())
                                .put("seq", lastSequenceNumber)
                        );

                        queueMessage(resumeObject);
                    }
                });
        return socket;
    }

    /**
     * Sets up a {@link WebSocket} instance to be used for the gateway connection.
     */
    @Contract("_ -> param1")
    private @NotNull WebSocket setupDisconnectedSocket(@NotNull WebSocket socket) {
        ExponentialBackoffLogic backoffReconnectLogic = new ExponentialBackoffLogic(bot);
        socket.setReEstablishConnection(backoffReconnectLogic.getFunction());
        backoffReconnectLogic.setAttemptReconnect((c) -> {
            new Thread(bot::clearMemberCaches, "djar--clearing-member-caches").start();
            return reconnecting;
        });

        socket.addOnDisconnectConsumer((cs) -> {
            if (bot.isDebug()) logger.info("[Gateway] Disconnected from gateway. Reason: " + cs.getCode() + ":" + cs.getReason());
            disconnectFlow(cs);
        });

        socket.addMessageConsumer((m) -> {
            try {
                this.handleTextMessage(m);
            } catch (Exception e) {
                logger.warning("[Gateway] Failed to handle text message. This is usually a bug, please report it on discord.jar's GitHub with this log message.");
                e.printStackTrace();
            }
        });
        return socket;
    }

    /**
     * Appends the relevant query parameters for the given URL.
     */
    private String appendGatewayQueryParams(String url) {
        url = url + "?v=" + bot.getApiVersion().getCode() + "&encoding=json";
        if (compressionType != GatewayTransportCompressionType.NONE) url = url + "&compress=" + compressionType.getValue();
        return url;
    }

    /**
     * Queues a message to be sent to the gateway.
     * <br>Messages will be sent after the HELLO event is received.
     * @param payload {@link JSONObject} containing the payload to send
     */
    public void queueMessage(@NotNull JSONObject payload) {
        if (bot.isDebug()) logger.info("[Gateway] Queued message: " + payload);
        while (readyForMessages) {
            socket.send(payload.toString());
            if (bot.isDebug()) {
                logger.info("[Gateway] Sent message: " + payload);
            }
            break;
        }
    }

    /**
     * Queues a message to be sent to the gateway.
     * <br>Messages will be sent after the READY event is received.
     * @param payload {@link JSONObject} containing the payload to send
     */
    public void queueMessageUntilReady(@NotNull JSONObject payload) {
        if (bot.isDebug()) logger.info("[Gateway] Queued message: " + payload);
        while (receivedReady) {
            socket.send(payload.toString());
            if (bot.isDebug()) {
                logger.info("[Gateway] Sent message: " + payload);
            }
            break;
        }
    }

    /**
     * Sends a request to the gateway to request guild members.
     * @param action {@link RequestGuildMembersAction} containing extra information about the request
     * @param future {@link CompletableFuture} that will be completed when the request is completed
     */
    public void requestGuildMembers(@NotNull RequestGuildMembersAction action, @NotNull CompletableFuture<List<Member>> future) {
        if (action.getQuery() == null & action.getUserIds() == null) {
            throw new IllegalArgumentException("You must provide either a query or a list of user ids");
        }

        if (bot.isDebug()) {
            logger.info("[Gateway] Requesting guild members...");
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
        payload.put("op", OpCodes.REQUEST_GUILD_MEMBERS.opCode);
        payload.put("d", dPayload);

        queueMessage(payload);

        memberRequestChunks.put(action.getNonce(), new Gateway.MemberChunkStorageWrapper(new ArrayList<>(), future));
    }

    public void sendVoicePayload(String guildId, String channelId, boolean selfMute, boolean selfDeaf) {
        JSONObject payload = new JSONObject();
        JSONObject dPayload = new JSONObject();
        dPayload.put("guild_id", guildId);
        dPayload.put("channel_id", channelId);
        dPayload.put("self_mute", selfMute);
        dPayload.put("self_deaf", selfDeaf);
        payload.put("op", OpCodes.VOICE_STATE_UPDATE.opCode);
        payload.put("d", dPayload);
        queueMessage(payload);
    }

    public void onVoiceStateUpdate(Consumer<VoiceState> consumer) {
        onVoiceStateUpdateListeners.add(consumer);
    }

    public void onVoiceServerUpdate(Consumer<VoiceServerUpdate> consumer) {
        onVoiceServerUpdateListeners.add(consumer);
    }

    public List<Consumer<VoiceState>> getOnVoiceStateUpdateListeners() {
        return onVoiceStateUpdateListeners;
    }

    public List<Consumer<VoiceServerUpdate>> getOnVoiceServerUpdateListeners() {
        return onVoiceServerUpdateListeners;
    }

    /**
     * Do not use this method - it is for internal use only.
     * @param status The status to set.
     */
    @Deprecated
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Retrieves the Gateway URL from Discord.
     * @return The Gateway URL.
     * @throws InterruptedException If there is a failure to sleep after a failed request.
     */
    private String getGatewayUrl() throws InterruptedException {
        String gatewayUrl;
        try {
            DiscordResponse response = null;
            try {
                response = new DiscordRequest(
                        new JSONObject(),
                        new HashMap<>(),
                        "/gateway",
                        bot,
                        "/gateway", RequestMethod.GET
                ).invoke();
            } catch (DiscordRequest.UnhandledDiscordAPIErrorException ignored) {}
            if (response == null || response.body() == null || !response.body().has("url")) {
                // In case the request fails, we can attempt to use the backup gateway URL instead.
                gatewayUrl = URLS.GATEWAY.BASE_URL;
            } else gatewayUrl = response.body().getString("url") + "/";
        } catch (Exception e) {
            logger.warning("[Gateway - Connection Flow] Failed to get gateway URL, waiting 5 seconds and retrying...");
            Thread.sleep(5000);
            return getGatewayUrl();
        }
        return gatewayUrl;
    }

    public void setReceivedReady(boolean receivedReady) {
        this.receivedReady = receivedReady;
    }

    /**
     * Returns resume info for the next resume attempt, or null if READY was not received yet.
     */
    @Nullable
    public ReconnectInfo getResumeInfo() {
        return resumeInfo;
    }

    /**
     * Returns when the last heartbeat was sent, or null if none were sent yet.
     */
    @Nullable
    public Date getLastHeartbeatSent() {
        return lastHeartbeatSent;
    }

    /**
     * Returns an array of estimated ping times in milliseconds based on heartbeat ACKs.
     */
    @NotNull
    public List<Long> getPingHistoryMs() {
        return pingHistoryMs;
    }

    private enum OpCodes {
        DISPATCH(0),
        HEARTBEAT(1),
        IDENTIFY(2),
        PRESENCE_UPDATE(3),
        VOICE_STATE_UPDATE(4),
        RESUME(6),
        RECONNECT(7),
        REQUEST_GUILD_MEMBERS(8),
        INVALID_SESSION(9),
        HELLO(10),
        HEARTBEAT_ACK(11),
        UNKNOWN(-1)
        ;
        private int opCode;

        OpCodes(int opCode) {
            this.opCode = opCode;
        }

        public int getOpCode() {
            return opCode;
        }

        public static OpCodes fromOpCode(int opCode) {
            for (OpCodes value : values()) {
                if (value.getOpCode() == opCode) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
    private enum CloseCode {

        UNKNOWN_ERROR(4000, true, true, null),
        UNKNOWN_OPCODE(4001, true, true, null),
        DECODE_ERROR(4002, true, true, null),
        NOT_AUTHENTICATED(4003, true, false, null),
        AUTHENTICATION_FAILED(4004, false, false, "Authentication failed - check your bot token."),
        ALREADY_AUTHENTICATED(4005, true, false, null),
        INVALID_SEQUENCE(4007, true, true, null),
        RATE_LIMITED(4008, true, true, null),
        SESSION_TIMEOUT(4009, true, false, null),
        INVALID_SHARD(4010, false, false, "Invalid shard - check your shard count and shard ID."),
        SHARDING_REQUIRED(4011, false, false, "Sharding required - if your bot is in over 2.5k guilds, you must enable sharding."),
        INVALID_API_VERSION(4012, false, false, "Invalid API version - check your bot version."),
        INVALID_INTENTS(4013, false, false, "Invalid intents - check your intents."),
        DISALLOWED_INTENTS(4014, false, false, "Disallowed intents - you tried to specify an intent that you have not enabled or are not whitelisted for."),
        UNKNOWN(-1, true, false, null),
        ;
        private int code;
        private boolean reconnect;
        private boolean resume;
        private String log;

        CloseCode(int code, boolean reconnect, boolean resume, String log) {
            this.code = code;
            this.reconnect = reconnect;
            this.resume = resume;
            this.log = log;
        }

        public int getCode() {
            return code;
        }

        public boolean shouldReconnect() {
            return reconnect;
        }

        public boolean shouldResume() {
            return resume;
        }

        public String getLog() {
            return log;
        }

        public static CloseCode fromCode(int code) {
            for (CloseCode value : values()) {
                if (value.getCode() == code) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
    public record ReconnectInfo(String sessionId, String token, String url) {}
    public record MemberChunkStorageWrapper(List<Member> members, CompletableFuture<List<Member>> future) {
        public void addMember(Member member) {
            members.add(member);
        }

        public void complete() {
            future.complete(members);
        }
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Builder builder(DiscordJar bot) {
        return new GatewayBuilder(bot);
    }

    public interface Builder {
        Gateway build();
        Gateway.Builder setShardCount(int shardCount);
        Gateway.Builder setShardId(int shardId);
        Gateway.Builder setTransportCompressionType(GatewayTransportCompressionType compressionType);
    }
    private static class GatewayBuilder implements Builder {
        private final DiscordJar bot;
        private int shardCount = 1;
        private int shardId = -1;
        private GatewayTransportCompressionType compressionType = GatewayTransportCompressionType.ZLIB_STREAM;

        public GatewayBuilder(DiscordJar bot) {
            this.bot = bot;
        }

        @Override
        public Gateway build() {
            return new Gateway(bot, shardCount, shardId, compressionType);
        }

        @Override
        public Gateway.Builder setShardCount(int shardCount) {
            this.shardCount = shardCount;
            return this;
        }

        @Override
        public Gateway.Builder setShardId(int shardId) {
            this.shardId = shardId;
            return this;
        }

        @Override
        public Gateway.Builder setTransportCompressionType(GatewayTransportCompressionType compressionType) {
            this.compressionType = compressionType;
            return this;
        }
    }


}
