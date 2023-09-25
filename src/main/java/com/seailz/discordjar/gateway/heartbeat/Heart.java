package com.seailz.discordjar.gateway.heartbeat;

import com.seailz.discordjar.gateway.Gateway;
import com.seailz.discordjar.gateway.GatewayFactory;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class Heart {

    private int interval;
    private GatewayFactory factory;
    private boolean active = true;

    public Heart(int interval, GatewayFactory factory) {
        this.interval = interval;
        this.factory = factory;
        begin();
    }

    private void begin() {
        sendFirst();
        startCycle();
    }

    private void startCycle() {
        if (factory.isDebug()) {
            Logger.getLogger("HeartbeatManager").info(
                    """
                            [HEARTBEAT] Starting heartbeat cycle with interval of %dms.
                         """
                            .formatted(interval)
            );
        }
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!factory.getSocket().isOpen()) {
                    deactivate();
                    break;
                }
                if (active) send();
            }
        }, "djar--heart-cycle").start();
    }

   private void sendFirst() {
       double jitter = new Random().nextDouble(1);
       double interval = this.interval * jitter;
         new Thread(() -> {
              try {
                Thread.sleep((long) interval);
                if (active) send();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
         }, "djar--first-heart").start();
   }

   public void forceHeartbeat() {
        send();
   }

    private void send() {
        factory.queueMessage(
                new JSONObject()
                    .put("op", 1)
                    .put("d", GatewayFactory.sequence)
        );

        Gateway.lastHeartbeatSent = new Date();
    }

    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }

}
