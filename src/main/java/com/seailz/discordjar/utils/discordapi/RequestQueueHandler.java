package com.seailz.discordjar.utils.discordapi;

import com.seailz.discordjar.DiscordJar;

/**
 * A class that handles the queue of requests to the Discord API.
 *
 * @author Seailz
 * @see com.seailz.discordjar.utils.discordapi.DiscordRequest
 * @since 1.0
 */
public class RequestQueueHandler {

    public RequestQueueHandler(DiscordJar jv) {
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

                    System.out.println(limit.remaining());
                    System.out.println(limit.resetAfter());
                    System.out.println(limit.limit());

                    jv.getQueuedRequests().remove(req);
                    req.invoke();
                }
            }
        }).start();
    }

}
