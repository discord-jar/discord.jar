package com.seailz.javadiscordwrapper.utils.discordapi;

import com.seailz.javadiscordwrapper.DiscordJv;

/**
 * A class that handles the queue of requests to the Discord API.
 *
 * @author Seailz
 * @since  1.0
 * @see   com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest
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

                    jv.getQueuedRequests().remove(req);
                    req.invoke();
                }
            }
        }).start();
    }

}
