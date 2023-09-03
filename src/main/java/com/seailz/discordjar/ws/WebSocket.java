package com.seailz.discordjar.ws;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Abstracts the Spring Websocket implementation to make it easier to use, understand, and maintain.
 */
public class WebSocket extends WebSocketListener {

    private final String url;
    private okhttp3.WebSocket ws;
    private final boolean debug;
    private final List<Consumer<String>> messageConsumers = new ArrayList<>();
    private final List<Consumer<CloseStatus>> onDisconnectConsumers = new ArrayList<>();
    private final List<Runnable> onConnectConsumers = new ArrayList<>();
    private Function<CloseStatus, Boolean> reEstablishConnection = (e) -> true;
    private static final byte[] ZLIB_SUFFIX = new byte[] { 0, 0, (byte) 0xff, (byte) 0xff };
    private static byte[] buffer = {};
    private static final Inflater inflator = new Inflater();
    private boolean open = false;

    public WebSocket(String url, boolean debug) {
        this.url = url;
        this.debug = debug;
    }

    public WebsocketAction<Void> connect() {
        // Start connection cycle
        WebsocketAction<Void> action = new WebsocketAction<>();
        try {
            connect(url);
        } catch (Exception e) {
            action.fail(new WebsocketExceptionError(e));
            return action;
        }
        action.complete(null);

        onConnectConsumers.forEach(Runnable::run);
        return action;
    }

    public void disconnect() {
        ws.close(1000, "Disconnecting");
    }

    public void send(String message) {
        ws.send(message);
    }

    @Override
    public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull String text) {
        messageConsumers.forEach(consumer -> {
            consumer.accept(text);
        });
    }

    public okhttp3.WebSocket getWs() {
        return ws;
    }

    @Override
    public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull ByteString text) {
        long start = System.currentTimeMillis();
        byte[] msg = text.toByteArray();
        if (buffer ==null) buffer = new byte[]{};
        byte[] extendedBuffer = new byte[buffer.length + msg.length];
        System.arraycopy(buffer, 0, extendedBuffer, 0, buffer.length);
        try {
            System.arraycopy(msg, 0, extendedBuffer, buffer.length, msg.length);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Reset buffer and try again
            e.printStackTrace();
            buffer = new byte[]{};
            onMessage(webSocket, text);
            return;
        }
        buffer = extendedBuffer;

        // Check if the last four bytes are equal to ZLIB_SUFFIX
        if (msg.length < 4 || !Arrays.equals(Arrays.copyOfRange(msg, msg.length - 4, msg.length), ZLIB_SUFFIX)) {
            return;
        }

        // Inflate the data
        inflator.setInput(buffer);

        byte[] result = new byte[0];
        byte[] tmp = new byte[1024];
        int count;
        while (true) {
            try {
                if ((count = inflator.inflate(tmp, 0, tmp.length)) == 0) break;
            } catch (DataFormatException e) {
                // Reset inflation and try again
                e.printStackTrace();
                inflator.reset();
                inflator.setInput(buffer);
                continue;
            }
            result = Arrays.copyOf(result, result.length + count);
            System.arraycopy(tmp, 0, result, result.length - count, count);
        }

        CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(result));
        String fullMessage = charBuffer.toString();

        // reset buffer to empty
        buffer = new byte[0];
        if (true) {
            Logger.getLogger("WS")
                    .info("[Decompressor] Inflated " + msg.length + " bytes to " + result.length + " bytes in " + (System.currentTimeMillis() - start) + "ms: " + fullMessage);
        }
        new Thread(() -> {
            onMessage(webSocket, fullMessage);
        }, "djar--handle-text-msg").start();
    }
    @Override
    public void onClosed(@NotNull okhttp3.WebSocket webSocket, int code, @NotNull String reason) {
        // Force session disconnect in case it failed to disconnect
        open = false;
        buffer = null;
        new Thread(() -> {
            onDisconnectConsumers.forEach(consumer -> consumer.accept(new CloseStatus(code, reason)));
        }, "djar--ws-disconnect-consumers").start();

        if (reEstablishConnection.apply(new CloseStatus(code, reason))) {
            try {
                connect(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows you to specify the logic for if a re-establishment of the connection should be attempted after a disconnect.
     */
    public void setReEstablishConnection(Function<CloseStatus, Boolean> reEstablishConnection) {
        this.reEstablishConnection = reEstablishConnection;
    }

    public void addMessageConsumer(Consumer<String> consumer) {
        messageConsumers.add(consumer);
    }

    public void addOnDisconnectConsumer(Consumer<CloseStatus> consumer) {
        onDisconnectConsumers.add(consumer);
    }

    public void addOnConnect(Runnable runnable) {
        onConnectConsumers.add(runnable);
    }

    public void removeMessageConsumer(Consumer<TextMessage> consumer) {
        messageConsumers.remove(consumer);
    }

    public void removeOnDisconnectConsumer(Consumer<CloseStatus> consumer) {
        onDisconnectConsumers.remove(consumer);
    }

    private void connect(String customUrl) throws ExecutionException, InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0,  TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url(customUrl)
                .build();
        this.ws = client.newWebSocket(request, this);

        open = true;
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();

//        WebSocketClient client = new StandardWebSocketClient();
//        this.client = client;
//        this.session = client.execute(this, new WebSocketHttpHeaders(), URI.create(customUrl)).get();
        // Allocate 25% of the bot's total **AVAILABLE** memory to the gateway.
//        if (newSystemForMemoryManagement) {
//            Runtime runtime = Runtime.getRuntime();
//            long maxMemory = runtime.maxMemory();
//
//            if (debug) {
//                Logger.getLogger("WS")
//                        .info("[WS] NSGFMM POTM:" + nsfgmmPercentOfTotalMemory + "%");
//                Logger.getLogger("WS")
//                        .info("[WS] " + "As NSGFMMPOTM is " + nsfgmmPercentOfTotalMemory + "%, the allocated memory will be " + (maxMemory * (nsfgmmPercentOfTotalMemory / 100)) + " bytes (" + ((maxMemory * (nsfgmmPercentOfTotalMemory / 100)) / 1000000) + " MB" + " NSGFM / 100 =" + (nsfgmmPercentOfTotalMemory / 100));
//            }
//            maxMemory = (long) (maxMemory * (nsfgmmPercentOfTotalMemory / 100));
//
//            if (debug) {
//                Logger.getLogger("WS")
//                        .info("[WS] " + "Allocated memory: " + maxMemory + " bytes (" + (maxMemory / 1000000) + " MB) / " + runtime.maxMemory() + " bytes (" + (runtime.maxMemory() / 1000000) + " MB)");
//            }
//
//            session.setTextMessageSizeLimit((int) maxMemory);
//            session.setBinaryMessageSizeLimit((int) maxMemory);
//        } else {
//            session.setTextMessageSizeLimit(100000000);
//            session.setBinaryMessageSizeLimit(100000000);
//        }
    }


    public class WebsocketAction<T> {
        private final CompletableFuture<T> onSuccessful = new CompletableFuture<>();
        private final CompletableFuture<WebsocketError> onFailed = new CompletableFuture<>();

        protected WebsocketAction() {}

        public WebsocketAction<T> onSuccess(Consumer<T> consumer) {
            onSuccessful.thenAccept(consumer);
            return this;
        }

        public WebsocketAction<T> onFailed(Consumer<WebsocketExceptionError> consumer) {
            onFailed.thenAccept((e) -> {
                if (e instanceof WebsocketExceptionError) consumer.accept((WebsocketExceptionError) e);
            });
            return this;
        }

        public WebsocketAction<T> onFailedDisconnect(Consumer<WebsocketDisconnectError> consumer) {
            onFailed.thenAccept((e) -> {
                if (e instanceof WebsocketDisconnectError) consumer.accept((WebsocketDisconnectError) e);
            });
            return this;
        }

        protected void complete(T t) {
            onSuccessful.complete(t);
        }

        protected void fail(WebsocketError t) {
            onFailed.complete(t);
        }

        public CompletableFuture<T> getOnSuccessful() {
            return onSuccessful;
        }

        public CompletableFuture<WebsocketError> getOnFailed() {
            return onFailed;
        }
    }

    public interface WebsocketError {}

    public class WebsocketExceptionError implements WebsocketError {
        private Throwable throwable;

        public WebsocketExceptionError(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    public class WebsocketDisconnectError implements WebsocketError {
        private int code;
        private String reason;

        public WebsocketDisconnectError(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }
    }

    public boolean isOpen() {
        return open;
    }
}
