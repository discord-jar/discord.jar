package com.seailz.discordjar.utils.discordapi;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.utils.URLS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DiscordRequest {


    private JSONObject body;
    private final HashMap<String, String> headers;
    private final String url;
    private final DiscordJar djv;
    private final String baseUrl;
    private final RequestMethod requestMethod;
    private JSONArray aBody;

    public DiscordRequest(JSONObject body, HashMap<String, String> headers, String url, DiscordJar djv, String baseUrl, RequestMethod requestMethod) {
        this.body = body;
        this.headers = headers;
        this.url = url;
        this.djv = djv;
        this.baseUrl = baseUrl;
        this.requestMethod = requestMethod;
    }
    public DiscordRequest(JSONArray body, HashMap<String, String> headers, String url, DiscordJar djv, String baseUrl, RequestMethod requestMethod) {
        this.aBody = body;
        this.headers = headers;
        this.url = url;
        this.djv = djv;
        this.baseUrl = baseUrl;
        this.requestMethod = requestMethod;
    }

    /**
     * Sends the request to the Discord API
     * If the request is rate-limited, it will be queued.
     * <p>
     * This method takes into account rate-limits, error codes, and the queue.
     *
     * @return The {@link DiscordResponse} from the Discord API
     */
    private DiscordResponse invoke(String contentType, boolean auth) throws UnhandledDiscordAPIErrorException {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

            if (djv.getRateLimits().containsKey(baseUrl) && djv.getRateLimits().get(baseUrl).remaining() == 0) {
                Logger.getLogger("DiscordJar").warning("[RATE LIMIT] Rate limit reached for " + baseUrl + ". Queuing request.");
                djv.getQueuedRequests().add(this);
                return new DiscordResponse(429, new JSONObject(), new HashMap<>(), null);
            }

            HttpRequest.Builder con = HttpRequest.newBuilder();

            con.uri(obj.toURI());

            String s = body != null ? body.toString() : aBody.toString();
            if (requestMethod == RequestMethod.POST) {
                con.POST(HttpRequest.BodyPublishers.ofString(s));
            } else if (requestMethod == RequestMethod.PATCH) {
                con.method("PATCH", HttpRequest.BodyPublishers.ofString(s));
            } else if (requestMethod == RequestMethod.PUT) {
                con.method("PUT", HttpRequest.BodyPublishers.ofString(s));
            }
            else if (requestMethod == RequestMethod.DELETE) {
                con.method("DELETE", HttpRequest.BodyPublishers.ofString(s));
            } else if (requestMethod == RequestMethod.GET) {
                con.GET();
            } else {
                con.method(requestMethod.name(), HttpRequest.BodyPublishers.ofString(s));
            }

            con.header("User-Agent", "discord.jar (https://github.com/discord-jar/, 1.0.0)");
            if (auth) con.header("Authorization", "Bot " + djv.getToken());
            if (contentType == null) con.header("Content-Type", "application/json");
            if (contentType != null) con.header("Content-Type", contentType); // switch to url search params
            headers.forEach(con::header);

            HttpRequest request = con.build();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int responseCode = response.statusCode();
            System.out.println(request.uri() + " " + request.method());

            HashMap<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, value.get(0)));

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
                            djv.getRateLimits().put(baseUrl, new RateLimit(
                                    rateLimit.limit(),
                                    rateLimit.remaining() - 1,
                                    rateLimit.resetAfter()
                            ));
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

            try {
                if (response.body().startsWith("[")) {
                    new JSONArray(response.body());
                } else {
                    new JSONObject(response.body());
                }
            } catch (JSONException err) {
                System.out.println(response.body());
            }

            if (responseCode == 200 || responseCode == 201) {

                var body = new Object();

                if (response.body().startsWith("[")) {
                    body = new JSONArray(response.body());
                } else {
                    body = new JSONObject(response.body());
                }

                return new DiscordResponse(responseCode, (body instanceof JSONObject) ? (JSONObject) body : null, headers, (body instanceof JSONArray) ? (JSONArray) body : null);
            }
            if (responseCode == 204) return null;
            if (responseCode == 429) {
                Logger logger = Logger.getLogger("DiscordJar");
                logger.warning("[RATE LIMIT] Rate limit exceeded, waiting for " + response.headers().map().get("Retry-After").get(0) + " seconds");
                djv.getRateLimits().put(baseUrl, new RateLimit(
                        Double.parseDouble(response.headers().map().get("X-RateLimit-Limit").get(0)),
                        Double.parseDouble(response.headers().map().get("X-RateLimit-Remaining").get(0)),
                        Double.parseDouble(response.headers().map().get("X-RateLimit-Reset-After").get(0))
                ));
                new Thread(() -> {
                    try {
                        Thread.sleep(Integer.parseInt(response.headers().map().get("Retry-After").get(0)));
                        this.invoke();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, "RateLimitQueue").start();
                return null;
            }

            if (responseCode == 201) return null;

            if (!auth && responseCode == 401) {
                return new DiscordResponse(401, null, null, null);
            }

            JSONObject error = new JSONObject(response.body());
            JSONArray errorArray;

            if (responseCode == 404) {
                Logger.getLogger("DISCORDJAR")
                        .warning("Received 404 error from the Discord API. It's likely that you're trying to access a resource that doesn't exist.");
                return new DiscordResponse(404, null, null, null);
            }

            throw new UnhandledDiscordAPIErrorException(
                    responseCode,
                    "Unhandled Discord API Error. Please report this to the developer of DiscordJar." + error
            );
        } catch (UnhandledDiscordAPIErrorException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DiscordResponse invoke(JSONObject body) {
        return invoke(null, true);
    }

    public DiscordResponse invokeNoAuth(JSONObject body) {
        return invoke(null, false);
    }

    public DiscordResponse invokeNoAuthCustomContent(String contentType) {
        return invoke(contentType, false);
    }

    public DiscordResponse invoke(JSONArray arr) {
        return invoke(null, true);
    }

    public DiscordResponse invoke() throws UnhandledDiscordAPIErrorException {
        return invoke(null, true);
    }

    public DiscordResponse invokeWithFiles(File... files) {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

            if (djv.getRateLimits().containsKey(baseUrl) && djv.getRateLimits().get(baseUrl).remaining() == 0) {
                Logger.getLogger("DiscordJar").warning("[RATE LIMIT] Rate limit reached for " + baseUrl + ". Queuing request.");
                djv.getQueuedRequests().add(this);
                return new DiscordResponse(429, new JSONObject(), new HashMap<>(), null);
            }

            HttpRequest.Builder con = HttpRequest.newBuilder();

            con.uri(obj.toURI());

            String boundary = "---1234567890";
            String crlf = "\r\n";
            List<byte[]> bodyParts = new ArrayList<>();
            bodyParts.add("--".getBytes(StandardCharsets.UTF_8));
            bodyParts.add(boundary.getBytes(StandardCharsets.UTF_8));

// Add JSON payload
            bodyParts.add((crlf + "Content-Disposition: form-data; name=\"payload_json\"" + crlf + "Content-Type: application/json" + crlf + crlf + this.body + crlf).getBytes(StandardCharsets.UTF_8));

// Add file attachments
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                byte[] fileContent = Files.readAllBytes(file.toPath());
                bodyParts.add("--".getBytes(StandardCharsets.UTF_8));
                bodyParts.add(boundary.getBytes(StandardCharsets.UTF_8));
                bodyParts.add((crlf + "Content-Disposition: form-data; name=\"files[" + i + "]\"; filename=\"" + file.getName() + "\"" + crlf + "Content-Type: " + Files.probeContentType(file.toPath()) + crlf + crlf).getBytes(StandardCharsets.UTF_8));
                bodyParts.add(fileContent);
                bodyParts.add(crlf.getBytes(StandardCharsets.UTF_8));
            }

            bodyParts.add("--".getBytes(StandardCharsets.UTF_8));
            bodyParts.add(boundary.getBytes(StandardCharsets.UTF_8));
            bodyParts.add("--".getBytes(StandardCharsets.UTF_8));

            byte[] body = new byte[bodyParts.stream().mapToInt(part -> part.length).sum()];
            int offset = 0;
            for (byte[] part : bodyParts) {
                System.arraycopy(part, 0, body, offset, part.length);
                offset += part.length;
            }

            if (requestMethod == RequestMethod.POST) {
                con.POST(HttpRequest.BodyPublishers.ofByteArray(body));
            } else if (requestMethod == RequestMethod.PATCH) {
                con.method("PATCH", HttpRequest.BodyPublishers.ofByteArray(body));
            } else if (requestMethod == RequestMethod.PUT) {
                con.method("PUT", HttpRequest.BodyPublishers.ofByteArray(body));
            } else if (requestMethod == RequestMethod.DELETE) {
                con.method("DELETE", HttpRequest.BodyPublishers.ofByteArray(body));
            } else if (requestMethod == RequestMethod.GET) {
                con.GET();
            } else {
                con.method(requestMethod.name(), HttpRequest.BodyPublishers.ofByteArray(body));
            }

            con.header("User-Agent", "discord.jar (https://github.com/discord-jar/discord.jar, 1.0.0)");
            con.header("Authorization", "Bot " + djv.getToken());
            con.header("Content-Type", "multipart/form-data; boundary=" + boundary);


            HttpRequest request = con.build();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            int responseCode = response.statusCode();
            System.out.println(request.uri() + " " + request.method());

            HashMap<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, value.get(0)));

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

            if (responseCode == 200 || responseCode == 201) {

                var bodyResponse = new Object();

                if (response.body().startsWith("[")) {
                    bodyResponse = new JSONArray(response.body());
                } else {
                    bodyResponse = new JSONObject(response.body());
                }

                return new DiscordResponse(responseCode, (bodyResponse instanceof JSONObject) ? (JSONObject) bodyResponse : null, headers, (bodyResponse instanceof JSONArray) ? (JSONArray) bodyResponse : null);
            }
            if (responseCode == 204) return null;
            if (responseCode == 429) {
                Logger logger = Logger.getLogger("DiscordJar");
                logger.warning("[RATE LIMIT] Rate limit exceeded, waiting for " + response.headers().map().get("Retry-After").get(0) + " seconds");
                djv.getRateLimits().put(baseUrl, new RateLimit(
                        Integer.parseInt(response.headers().map().get("X-RateLimit-Limit").get(0)),
                        Integer.parseInt(response.headers().map().get("X-RateLimit-Remaining").get(0)),
                        Integer.parseInt(response.headers().map().get("X-RateLimit-Reset-After").get(0))
                ));
                new Thread(() -> {
                    try {
                        Thread.sleep(Integer.parseInt(response.headers().map().get("Retry-After").get(0)));
                        this.invoke();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, "RateLimitQueue").start();
                return null;
            }


            JSONObject error = new JSONObject(response.body());
            JSONArray errorArray;

            try {
                errorArray = error.getJSONArray("errors").getJSONArray(3);
            } catch (JSONException e) {
                try {
                    errorArray = error.getJSONArray("errors").getJSONArray(1);
                } catch (JSONException ex) {
                    try {
                        errorArray = error.getJSONArray("errors").getJSONArray(0);
                    } catch (JSONException exx) {
                        throw new UnhandledDiscordAPIErrorException(
                                responseCode,
                                "Unhandled Discord API Error. Please report this to the developer of DiscordJar." + error
                        );
                    }
                }
            }

            errorArray.forEach(o -> {
                JSONObject errorObject = (JSONObject) o;
                throw new DiscordAPIErrorException(
                        responseCode,
                        errorObject.getString("code"),
                        errorObject.getString("message"),
                        error.toString()
                );
            });
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

    public static class DiscordAPIErrorException extends RuntimeException {
        public DiscordAPIErrorException(int code, String errorCode, String error, String body) {
            super("DiscordAPI [Error " + HttpStatus.valueOf(code) + "]: " + errorCode + " " + error + " " + body);
        }
    }

    public static class UnhandledDiscordAPIErrorException extends RuntimeException {
        public UnhandledDiscordAPIErrorException(int code, String error) {
            super("DiscordAPI [Error " + HttpStatus.valueOf(code) + "]: " + error);
        }
    }

    public JSONObject body() {
        return body;
    }

    public JSONArray array() {
        return aBody;
    }

    public HashMap<String, String> headers() {
        return headers;
    }

    public String url() {
        return url;
    }


}
