package com.seailz.discordjv.gateway.heartbeat;

import com.seailz.discordjv.gateway.GatewayFactory;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * This class is responsible for sending heartbeats to the Discord API.
 * This class is used internally and should not be used by the end user.
 *
 * @author Seailz
 * @see com.seailz.discordjv.gateway.GatewayFactory
 * @since 1.0
 */
public class HeartbeatCycle {

    private final int interval;
    private final GatewayFactory factory;
    private boolean shouldBeSendingHeartbeats;

    public HeartbeatCycle(int interval, GatewayFactory factory) throws InterruptedException {
        this.interval = interval;
        this.factory = factory;
        sendFirst();
        start();
        // sessionCheck();
        shouldBeSendingHeartbeats = true;
    }

    public void sendHeartbeat() throws IOException, ExecutionException, InterruptedException {
        JSONObject payload = new JSONObject();
        payload.put("op", 1);
        payload.put("d", GatewayFactory.getLastSequence());

        if (!factory.getClientSession().isOpen()) {
            System.out.println("GATEWAY IS CLOSED");
            factory.getClientSession().close(CloseStatus.GOING_AWAY);
            factory.startAgain();

            factory.heartbeatCycle = null;
            shouldBeSendingHeartbeats = false;
            return;
        }

        if (shouldBeSendingHeartbeats) factory.getClientSession().sendMessage(new TextMessage(payload.toString()));
    }

    public void sendFirst() throws InterruptedException {
        double jitter = new Random().nextDouble(1);
        double interval = this.interval * jitter;

        new Thread(() -> {
            try {
                Thread.sleep((long) interval);
                System.out.println("sending first");
                sendHeartbeat();
            } catch (InterruptedException | IOException | ExecutionException e) {
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
        new Thread(() -> {
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
