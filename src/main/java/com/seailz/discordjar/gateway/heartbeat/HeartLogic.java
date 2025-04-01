package com.seailz.discordjar.gateway.heartbeat;

import com.seailz.discordjar.gateway.Gateway;
import com.seailz.discordjar.ws.WSPayloads;
import com.seailz.discordjar.ws.WebSocket;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Every animal has a heart, and as we all know that websockets are animals, they also have hearts.
 * This is a stolen joke from Discord4J (SORRY)
 *
 * @author SeaIlz
 */
public class HeartLogic {

    private WebSocket socket;
    private long interval;
    private long lastSequence = -1;
    private final Map<UUID, Boolean> isInstanceStillRunning = new HashMap<>();
    boolean running = true;
    private final Gateway gateway;

    public HeartLogic(WebSocket socket, long interval, Gateway gateway) {
        this.interval = interval;
        this.socket = socket;
        this.gateway = gateway;
    }

    public HeartLogic(WebSocket socket, Gateway gateway) {
        this.socket = socket;
        this.gateway = gateway;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setLastSequence(long sequence) {
        this.lastSequence = sequence;
    }

    public void restart() {
        running = false;
        startCycle();
    }

    public void stop() {
        running = false;
    }

    public void startCycle() {
        running = true;
    }

    public void setSocket(WebSocket socket) {
        this.socket = socket;
    }

    public void forceHeartbeat() {
        socket.send(
            WSPayloads.HEARBEAT.fill(lastSequence == -1 ? JSONObject.NULL : lastSequence).toString()
        );
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (!running) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                try {
                    socket.send(
                        WSPayloads.HEARBEAT.fill(lastSequence == -1 ? JSONObject.NULL : lastSequence).toString()
                    );
                    gateway.lastHeartbeatSent = new Date();
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "djar--heart-cycle");
        thread.start();
    }

}
