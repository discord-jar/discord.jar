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

    public HeartbeatCycle(int interval, GatewayFactory factory) {
        this.interval = interval;
        this.factory = factory;
        start();
        // sessionCheck();
    }

    public void sendHeartbeat() throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("op", 1);
        payload.put("d", factory.getLastSequence());
        factory.getClientSession().sendMessage(new TextMessage(payload.toString()));
    }

    public void sendFirst() {
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
    }

    public void start() {
        sendFirst();
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                    sendHeartbeat();
                } catch (InterruptedException | IOException e) {
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

                    } catch (ExecutionException | InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

}
