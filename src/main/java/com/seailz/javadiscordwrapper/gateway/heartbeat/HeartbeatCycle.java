package com.seailz.javadiscordwrapper.gateway.heartbeat;

import com.seailz.javadiscordwrapper.gateway.GatewayFactory;
import org.json.JSONObject;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * This class is responsible for sending heartbeats to the Discord API.
 * This class is used internally and should not be used by the end user.
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.gateway.GatewayFactory
 */
public class HeartbeatCycle {

    private int interval;
    private GatewayFactory factory;
    private boolean shouldBeSendingHeartbeats;

    public HeartbeatCycle(int interval, GatewayFactory factory) throws InterruptedException {
        this.interval = interval;
        this.factory = factory;
        start();
        // sessionCheck();
        shouldBeSendingHeartbeats = true;
    }

    public void sendHeartbeat() throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("op", 1);
        payload.put("d", factory.getLastSequence());
        factory.getClientSession().sendMessage(new TextMessage(payload.toString()));
    }

    public void sendFirst() throws InterruptedException {
        double jitter = new Random().nextDouble(1);
        double interval = this.interval * jitter;

        new Thread(() -> {
            try {
                Thread.sleep((long) interval);
                sendHeartbeat();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void start() throws InterruptedException {
        sendFirst();
        new Thread(() -> {
            while (shouldBeSendingHeartbeats) {
                try {
                    Thread.sleep(interval);
                    try {
                        sendHeartbeat();
                    } catch (IllegalArgumentException e) {
                        shouldBeSendingHeartbeats = false;
                        factory.reconnect();
                    }
                } catch (InterruptedException | IOException | ExecutionException e) {
                    shouldBeSendingHeartbeats = false;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sessionCheck() {
        new Thread(()->  {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!factory.getClientSession().isOpen()) {
                    try {
                        factory.reconnect();
                        shouldBeSendingHeartbeats = false;
                    } catch (ExecutionException | InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
