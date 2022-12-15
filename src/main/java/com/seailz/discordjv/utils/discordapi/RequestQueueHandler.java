package com.seailz.discordjv.utils.discordapi;

import com.seailz.discordjv.DiscordJv;

/**
 * A class that handles the queue of requests to the Discord API.
 *
 * @author Seailz
 * @see com.seailz.discordjv.utils.discordapi.DiscordRequest
 * @since 1.0
 */
public class RequestQueueHandler {

    public RequestQueueHandler(DiscordJv jv) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (jv.getQueuedRequests().isEmpty()) continue;
                for (DiscordRequest req : jv.getQueuedRequests()) {
                    if (req.getRateLimit().isEmpty()) continue;
                    RateLimit limit = req.getRateLimit().get();
                    if (limit.remaining() == 0) continue;

                    if(limit.resetAfter() == 0) {
                        jv.getQueuedRequests().remove(req);
                        req.invoke();
                    }
                }
            }
        }).start();
    }

}
