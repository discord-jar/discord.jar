package com.seailz.discordjv.utils.discordapi;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.URLS;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class that represents a Discord request
 *
 * @param body    The body of the request
 * @param headers The headers of the request
 * @param url     The URL of the request
 * @param baseUrl The endpoint URL of the request
 * @author Seailz
 * @see DiscordJv
 * @since 1.0
 */
public record DiscordRequest(
        JSONObject body,
        HashMap<String, String> headers,
        String url,
        DiscordJv djv,
        String baseUrl,
        RequestMethod requestMethod
) {

    /**
     * Sends the request to the Discord API
     * If the request is rate-limited, it will be queued.
     * <p>
     * This method takes into account rate-limits, error codes, and the queue.
     *
     * @return The {@link DiscordResponse} from the Discord API
     */
    public DiscordResponse invoke() {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

            if (djv.getRateLimits().containsKey(baseUrl) && djv.getRateLimits().get(baseUrl).remaining() == 0) {
                Logger.getLogger("DiscordJv").warning("[RATE LIMIT] Rate limit reached for " + baseUrl + ". Queuing request.");
                djv.getQueuedRequests().add(this);
                return new DiscordResponse(429, new JSONObject(), new HashMap<>(), null);
            }

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(requestMethod.toString());
            con.setRequestProperty("User-Agent", "DiscordJv (www.seailz.com, 1.0)");
            con.setRequestProperty("Authorization", "Bot " + djv.getToken());
            con.setRequestProperty("Content-Type", "application/json");
            headers.forEach(con::setRequestProperty);

            byte[] out = body.toString().getBytes(StandardCharsets.UTF_8);

            if (requestMethod == RequestMethod.POST || requestMethod == RequestMethod.PATCH) {
                con.setDoOutput(true);
                try (OutputStream os = con.getOutputStream()) {
                    os.write(out);
                }
            }

            con.connect();
            int responseCode = con.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                HashMap<String, String> headers = new HashMap<>();
                for (String key : con.getHeaderFields().keySet())
                    headers.put(key, con.getHeaderField(key));

                if (headers.containsKey("X-RateLimit-Limit") &&
                        headers.containsKey("X-RateLimit-Remaining") &&
                        headers.containsKey("X-RateLimit-Reset-After")) {

                    djv.getRateLimits().remove(baseUrl);
                    djv.getRateLimits().put(baseUrl, new RateLimit(
                            Integer.parseInt(headers.get("X-RateLimit-Limit")),
                            Integer.parseInt(headers.get("X-RateLimit-Remaining")),
                            Integer.parseInt(headers.get("X-RateLimit-Reset-After"))
                    ));
                }
                // remove after rate limit is over
                new Thread(() -> {
                    boolean running = true;
                    while (running) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        if (djv.getRateLimits().containsKey(baseUrl)) {
                            RateLimit rateLimit = djv.getRateLimits().get(baseUrl);
                            if (rateLimit.remaining() == rateLimit.limit() || rateLimit.resetAfter() == 0) {
                                djv.getRateLimits().remove(baseUrl);
                                running = false;
                                continue;
                            }

                            djv.getRateLimits().remove(baseUrl);
                            djv.getRateLimits().put(baseUrl, new RateLimit(
                                    rateLimit.limit(),
                                    rateLimit.remaining() - 1,
                                    rateLimit.resetAfter()
                            ));
                        }
                    }
                }).start();

                var body = new Object();

                if (response.toString().startsWith("["))
                    body = new JSONArray(response.toString());
                else
                    body = new JSONObject(response.toString());

                return new DiscordResponse(responseCode, (body instanceof JSONObject) ? (JSONObject) body : null, headers, (body instanceof JSONArray) ? (JSONArray) body : null);
            }
            if (responseCode == 204) return null;
            if (responseCode == 429) {
                Logger logger = Logger.getLogger("DiscordJv");
                logger.warning("[RATE LIMIT] Rate limit exceeded, waiting for " + con.getHeaderField("Retry-After") + " seconds");
                new Thread(() -> {
                    try {
                        Thread.sleep(Integer.parseInt(con.getHeaderField("Retry-After")));
                        this.invoke();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, "RateLimitQueue").start();
                return null;
            }

            if (responseCode == 201) return null;

            throw new ResponseStatusException(org.springframework.http.HttpStatus.valueOf(responseCode),
                    con.getResponseMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the ratelimit info for the current endpoint
     */
    public Optional<RateLimit> getRateLimit() {
        return Optional.ofNullable(djv.getRateLimits().get(baseUrl));
    }

}
