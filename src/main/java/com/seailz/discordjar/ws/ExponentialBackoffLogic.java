package com.seailz.discordjar.ws;

import org.springframework.web.socket.CloseStatus;

import java.time.Instant;
import java.time.Duration;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Defines logic for a reconnect attempt based on increasing intervals - doubling each time.
 * <br>This is the recommended usage for reconnecting to a websocket. Additional logic can be added by using the attemptReconnect function to override the default logic.
 * <br>If true is returned in attemptReconnect, the reconnect will be attempted after the interval. If false is returned, the reconnect will not be attempted.
 * @author Seailz
 */
public class ExponentialBackoffLogic {

    private Function<CloseStatus, Boolean> attemptReconnect = (e) -> true;
    // If this interval is reached, the reconnect will be attempted at the same interval until it succeeds.
    private final int maxInterval = 60000;
    private int interval = 500;
    private int attempts = 0;

    private Instant lastCallTime = Instant.now();

    public void setAttemptReconnect(Function<CloseStatus, Boolean> attemptReconnect) {
        this.attemptReconnect = attemptReconnect;
    }

    public Function<CloseStatus, Boolean> getFunction() {
        return closeStatus -> {
            if (!attemptReconnect.apply(closeStatus)) {
                return false;
            }
            if (Duration.between(lastCallTime, Instant.now()).getSeconds() <= 60) {
                if (interval < maxInterval)
                    interval = Math.min(interval * 2, maxInterval);
            } else {
                interval = 1000;
                attempts = 0;
            }

            attempts++;

            try {
                Logger.getLogger("ExponentialBackoffLogic").info("[WebSocket] Waiting " + interval + "ms before attempting reconnect. Attempt " + attempts);
                Thread.sleep(interval);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return true;
            } finally {
                lastCallTime = Instant.now();
            }
        };
    }


}
