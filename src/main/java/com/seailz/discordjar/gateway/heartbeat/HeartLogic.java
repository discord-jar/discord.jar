package com.seailz.discordjar.gateway.heartbeat;

import com.seailz.discordjar.gateway.GatewayFactory;
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

    private final WebSocket socket;
    private final Map<UUID, Boolean> isInstanceStillRunning = new HashMap<>();
    private long interval;
    private long lastSequence = -1;

    public HeartLogic(WebSocket socket, long interval) {
        this.interval = interval;
        this.socket = socket;
    }

    public HeartLogic(WebSocket socket) {
        this.socket = socket;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setLastSequence(long sequence) {
        this.lastSequence = sequence;
    }

    public void restart() {
        isInstanceStillRunning.forEach((uuid, aBoolean) -> isInstanceStillRunning.put(uuid, false));
        start();
    }

    public void stop() {
        isInstanceStillRunning.forEach((uuid, aBoolean) -> isInstanceStillRunning.put(uuid, false));
    }

    public void forceHeartbeat() {
        socket.send(
                WSPayloads.HEARBEAT.fill(lastSequence == -1 ? JSONObject.NULL : lastSequence).toString()
        );
    }

    public void start() {
        Thread thread = new Thread(() -> {
            UUID uuid = UUID.randomUUID();
            isInstanceStillRunning.put(uuid, true);
            while (true) {
                if (!isInstanceStillRunning.get(uuid)) {
                    isInstanceStillRunning.remove(uuid);
                    return;
                }
                try {
                    socket.send(
                            WSPayloads.HEARBEAT.fill(lastSequence == -1 ? JSONObject.NULL : lastSequence).toString()
                    );
                    GatewayFactory.lastHeartbeatSent = new Date();
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "djar--heart-cycle");
        thread.start();
    }

}
