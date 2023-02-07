package com.seailz.discordjv.gateway.heartbeat;

import com.seailz.discordjv.gateway.GatewayFactory;
import org.json.JSONObject;

import java.util.Random;
import java.util.logging.Logger;

public class HeartbeatManager {

    private int interval;
    private GatewayFactory factory;
    private boolean active = true;

    public HeartbeatManager(int interval, GatewayFactory factory) {
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
                if (!factory.getSession().isOpen()) {
                    Logger.getLogger("HeartbeatManager").warning(
                            """
                                    [HEARTBEAT] Session is closed but heartbeat manager hasn't been killed.
                                    This is weird but probably not a problem.
                                    If you don't see any disconnect messages below, please report this to discord.jar's GitHub.
                                    For now, this heartbeat manager instance will be deactivated to avoid errors.
                                 """
                    );
                    Logger.getLogger("HeartbeatManager").info(
                            """
                                    [HEARTBEAT] If the above message is appearing frequently, and no errors are being thrown
                                    below, let Seailz#0001 know.\040\040\040
                                 """
                    );
                    deactivate();
                    break;
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
