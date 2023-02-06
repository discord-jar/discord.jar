package com.seailz.discordjv.gateway.heartbeat;

import com.seailz.discordjv.gateway.GatewayFactory;
import org.json.JSONObject;

import java.util.Random;

public class HeartbeatManager {

    private int interval;
    private GatewayFactory factory;
    private boolean active = true;

    public HeartbeatManager(int interval, GatewayFactory factory) {
        this.interval = interval;
        System.out.println(interval);
        this.factory = factory;
        begin();
    }

    private void begin() {
        sendFirst();
        startCycle();
    }

    private void startCycle() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (active) send();
            }
        }).start();
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
         }).start();
   }

   public void forceHeartbeat() {
        send();
   }

    private void send() {
        factory.sendPayload(
                new JSONObject()
                    .put("op", 1)
                    .put("d", GatewayFactory.sequence)
        );
    }

    public void deactivate() {
        active = false;
    }

    public void activate() {
        active = true;
    }

}
