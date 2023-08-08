package com.seailz.discordjar.ws;

import org.json.JSONObject;

import java.util.*;

/**
 * Every animal has a heart, and as we all know that websockets are animals, they also have hearts.
 * This is a stolen joke from Discord4J (SORRY)
 *
 * @author SeaIlz
 */
public class HeartLogic {

    private final WebSocket socket;
    private long interval;
    private long lastSequence = -1;
    private final Map<UUID, Boolean> isInstanceStillRunning = new HashMap<>();

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
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
