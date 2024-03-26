package com.seailz.discordjar.utils.rest;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.errors.ErrorTreeReader;
import com.seailz.discordjar.utils.rest.ratelimit.Bucket;
import okhttp3.Response;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class DiscordRequest {


    private JSONObject body;
    private final HashMap<String, String> headers;
    private final String url;
    private final DiscordJar djv;
    private final String baseUrl;
    private final RequestMethod requestMethod;
    private JSONArray aBody;
    // HashMap of endpoint to whether or not it can be requested (rate limited)
    private static HashMap<String, Boolean> canRequest = new HashMap<>();

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

    public DiscordResponse queueRequest(double resetAfter, String endpoint, boolean auth, String contentType) throws UnhandledDiscordAPIErrorException {
        canRequest.put(endpoint, false);
        try {
            Thread.sleep((long) (resetAfter * 1000));
        } catch (InterruptedException e) {
                e.printStackTrace();
        }

        new Thread(() -> {
            try {
                Thread.sleep((long) (resetAfter * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canRequest.put(endpoint, true);
        }, "djar--request-delay-reset-request").start();
        return invoke(contentType, auth);
    }

    public DiscordResponse invokeNoDiscordJar(String token) {
        assert djv == null;
        try {
            String url = URLS.BASE_URL + this.url;
            OkHttpClient client = new OkHttpClient();

            Request.Builder requestBuilder = new Request.Builder().url(url);

            String s = body != null ? body.toString() : aBody.toString();
            RequestBody requestBody;

            String contentType = "application/json";

            if (requestMethod == RequestMethod.POST) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.post(requestBody);
            } else if (requestMethod == RequestMethod.PATCH) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.patch(requestBody);
            } else if (requestMethod == RequestMethod.PUT) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.put(requestBody);
            } else if (requestMethod == RequestMethod.DELETE) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.delete(requestBody);
            } else if (requestMethod == RequestMethod.GET) {
                requestBuilder.get();
            } else {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.method(requestMethod.name(), requestBody);
            }

            requestBuilder.addHeader("User-Agent", "DiscordBot (https://github.com/discord-jar/, 1.0.0)");
            requestBuilder.addHeader("Authorization", "Bot " + token);
            requestBuilder.addHeader("Content-Type", contentType);
            headers.forEach(requestBuilder::addHeader);

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();

            int responseCode = response.code();
            String sb = response.body().string();
            HashMap<String, String> headers = new HashMap<>();
            Headers responseHeaders = response.headers();
            for (String name : responseHeaders.names()) {
                headers.put(name, responseHeaders.get(name));
            }

            try {
                if (sb.startsWith("[")) {
                    new JSONArray(sb);
                } else {
                    new JSONObject(sb);
                }
            } catch (JSONException err) {
                System.out.println(sb);
            }

            if (responseCode == 200 || responseCode == 201) {
                Object body;
                if (sb.startsWith("[")) {
                    body = new JSONArray(sb);
                } else {
                    try {
                        body = new JSONObject(sb);
                    } catch (JSONException err) {
                        throw new DiscordUnexpectedError(new RuntimeException("Invalid JSON response from Discord API: " + sb));
                    }
                }

                return new DiscordResponse(responseCode, (body instanceof JSONObject) ? (JSONObject) body : null, headers, (body instanceof JSONArray) ? (JSONArray) body : null);
            }
            if (responseCode == 204) {
                return null;
            }

            if (responseCode == 401) {
                return new DiscordResponse(401, null, null, null);
            }

            if (responseCode == 404) {
                Logger.getLogger("DISCORDJAR").warning("Received 404 error from the Discord API. It's likely that you're trying to access a resource that doesn't exist.");
                System.out.println(sb);
                return new DiscordResponse(404, null, null, null);
            }

            throw new UnhandledDiscordAPIErrorException(new JSONObject(sb), responseCode);
        } catch (IOException | UnhandledDiscordAPIErrorException e) {
            // attempt gateway reconnect
            throw new DiscordUnexpectedError(e);
        }
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
            if (canRequest.get(url) != null && !canRequest.get(url)) {
                // We can't request, so we'll wait our turn.
                while (!canRequest.get(url)) {
                    Thread.sleep(1);
                }
            }
            canRequest.put(url, false);

            Bucket requestBucket = djv.getBucketForUrl(url);
            String uuid = "";
            if (requestBucket != null) {
                // Bucket exists, lets make sure we wait our turn.
                if (djv.isDebug()) {
                    System.out.println("Waiting for request clearance...");
                }
                uuid = String.valueOf(requestBucket.awaitClearance());
            } else {
                if (djv.isDebug()) {
                    System.out.println("No bucket found for " + url);
                }
            }

            if (djv.isDebug()) {
                System.out.println("Cleared for launch");
            }

            OkHttpClient client = new OkHttpClient();

            Request.Builder requestBuilder = new Request.Builder().url(url);

            String s = body != null ? body.toString() : aBody.toString();
            RequestBody requestBody;

            if (contentType == null) {
                contentType = "application/json";
            }

            if (requestMethod == RequestMethod.POST) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.post(requestBody);
            } else if (requestMethod == RequestMethod.PATCH) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.patch(requestBody);
            } else if (requestMethod == RequestMethod.PUT) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.put(requestBody);
            } else if (requestMethod == RequestMethod.DELETE) {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.delete(requestBody);
            } else if (requestMethod == RequestMethod.GET) {
                requestBuilder.get();
            } else {
                requestBody = RequestBody.create(MediaType.parse(contentType), s);
                requestBuilder.method(requestMethod.name(), requestBody);
            }

            requestBuilder.addHeader("User-Agent", "DiscordBot (https://github.com/discord-jar/, 1.0.0)");
            if (auth) {
                requestBuilder.addHeader("Authorization", "Bot " + djv.getToken());
            }
            if (contentType != null) {
                requestBuilder.addHeader("Content-Type", contentType);
            }
            headers.forEach((key, value) -> requestBuilder.addHeader(key, value));

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            canRequest.put(url, true);

            int responseCode = response.code();
            String sb = response.body().string();
            if (djv.isDebug()) {
                System.out.println(uuid + " " + request.method() + " " + request.url() + " with " + (this.body == null ? this.aBody : this.body) + " returned " + responseCode + " with " + sb);
            }
            HashMap<String, String> headers = new HashMap<>();
            Headers responseHeaders = response.headers();
            for (String name : responseHeaders.names()) {
                headers.put(name, responseHeaders.get(name));
            }

            // All is said and done, let's get the rate-limit bucket up to date.
            // If the bucket doesn't exist, it will be created.
            if (responseHeaders.get("X-RateLimit-Bucket") != null) {
                String id = responseHeaders.get("X-RateLimit-Bucket");
                int limit = Integer.parseInt(responseHeaders.get("X-RateLimit-Limit"));
                int remaining = Integer.parseInt(responseHeaders.get("X-RateLimit-Remaining"));
                BigDecimal reset = new BigDecimal(responseHeaders.get("X-RateLimit-Reset"));
                float resetAfter = Float.parseFloat(responseHeaders.get("X-RateLimit-Reset-After"));

                if (djv.isDebug()) {
                    System.out.println("Updating bucket " + id + " with limit " + limit + ", remaining " + remaining + ", reset " + reset + ", resetAfter " + resetAfter);
                }

                if (djv.getBucket(id) == null) {
                    if (djv.isDebug()) {
                        System.out.println("Creating new bucket " + id + " with limit " + limit + ", remaining " + remaining + ", reset " + reset + ", resetAfter " + resetAfter);
                        System.out.println("COMMANDS: " + url.contains("commands"));
                    }
                    djv.updateBucket(id, new Bucket(id, limit, remaining, (reset.multiply(new BigDecimal(1000)).longValue()), resetAfter, djv.isDebug()));
                } else {
                    Bucket bucket = djv.getBucket(id);
                    bucket.setLimit(limit);
                    bucket.setResetAfter((long) (resetAfter * 1000));
                    bucket.setReset(reset.multiply(new BigDecimal(1000)).longValue());
                    bucket.setRemaining(remaining);
                    djv.removeBucket(djv.getBucket(id));
                    djv.updateBucket(id, bucket.addAffectedRoute(url));
                }
            }

            if (responseCode == 429) {
                if (djv.isDebug()) {
                    Logger.getLogger("RateLimit").warning("[RATE LIMIT] Rate limit has been exceeded. Please make sure you are not sending too many requests.");
                }

                JSONObject body = new JSONObject(sb);
                if (!body.has("retry_after")) {
                    Logger.getLogger("RateLimit")
                            .severe("[Ratelimiting] It's likely that you've hit a Cloudflare rate limit.");
                    System.out.println(body);
                    return new DiscordResponse(429, body, headers, null);
                }

                float retryAfter = body.getFloat("retry_after");
                if (retryAfter == -1) {
                    Logger.getLogger("RateLimit").warning("[RATE LIMIT] Invalid rate limit response (?) - please contact Discord support. " + sb);
                    return new DiscordResponse(429, body, headers, null);
                }

                Bucket bucket = djv.getBucketForUrl(url);
                if (bucket != null) {
                    bucket.await((long) (retryAfter * 1000));
                }

                return queueRequest(retryAfter, url, auth, contentType);
            }

            if (responseCode == 200 || responseCode == 201) {
                Object body;
                String responseBody = sb;
                if (responseBody.startsWith("[")) {
                    body = new JSONArray(responseBody);
                } else {
                    try {
                        body = new JSONObject(responseBody);
                    } catch (JSONException err) {
                        throw new DiscordUnexpectedError(new RuntimeException("Invalid JSON response from Discord API: " + responseBody));
                    }
                }

                return new DiscordResponse(responseCode, (body instanceof JSONObject) ? (JSONObject) body : null, headers, (body instanceof JSONArray) ? (JSONArray) body : null);
            }
            if (responseCode == 204) {
                return null;
            }

            if (responseCode == 401 && !auth) {
                return new DiscordResponse(401, null, null, null);
            }

            if (responseCode == 404) {
                String message = null;
                try {
                    JSONObject json = new JSONObject(sb);
                    if (json.has("message")) {
                        message = json.getString("message");
                    }
                } catch (JSONException ignored) {
                }
//                Logger.getLogger("DiscordJar")
//                        .warning("[REST] 404: " + message);
                return new DiscordResponse(404, null, null, null);
            }

            throw new UnhandledDiscordAPIErrorException(new JSONObject(sb), responseCode);
        } catch (IOException e) {
            // attempt gateway reconnect
            throw new DiscordUnexpectedError(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class DiscordUnexpectedError extends RuntimeException {
        public DiscordUnexpectedError(Throwable throwable) {
            super(throwable);
        }
    }

    public DiscordResponse invoke(JSONObject body) throws UnhandledDiscordAPIErrorException {
        return invoke(null, true);
    }

    public DiscordResponse invokeNoAuth(JSONObject body) throws UnhandledDiscordAPIErrorException {
        return invoke(null, false);
    }

    public DiscordResponse invokeNoAuthCustomContent(String contentType) throws UnhandledDiscordAPIErrorException {
        return invoke(contentType, false);
    }

    public DiscordResponse invoke(JSONArray arr) throws UnhandledDiscordAPIErrorException {
        return invoke(null, true);
    }

    public DiscordResponse invoke() throws UnhandledDiscordAPIErrorException {
        return invoke(null, true);
    }

    public DiscordResponse invokeWithFiles(File... files) {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

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

            con.header("User-Agent", "DiscordBot (https://github.com/discord-jar/discord.jar, 1.0.0)");
            con.header("Authorization", "Bot " + djv.getToken());
            con.header("Content-Type", "multipart/form-data; boundary=" + boundary);


            HttpRequest request = con.build();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            int responseCode = response.statusCode();

            HashMap<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, value.get(0)));


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


            JSONObject error = new JSONObject(response.body());

            throw new UnhandledDiscordAPIErrorException(error, responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class DiscordAPIErrorException extends RuntimeException {
        public DiscordAPIErrorException(UnhandledDiscordAPIErrorException e) {
            super(ErrorTreeReader.readErrorTree(e.getBody(), e.code));
        }
    }

    public static class UnhandledDiscordAPIErrorException extends Exception {
        private int code;
        private JSONObject body;
        private String error;
        private int httpCode;
        public UnhandledDiscordAPIErrorException(JSONObject body, int httpCode) {
            this.body = body.has("errors") ?  body.getJSONObject("errors") : body;
            this.code = body.getInt("code");
            this.error = body.getString("message");
            this.httpCode = httpCode;
            Logger.getLogger("discord.jar")
                    .warning(ErrorTreeReader.readErrorTree(body, httpCode));

            printStackTrace();
        }

        public int getCode() {
            return code;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public JSONObject getBody() {
            return body;
        }

        public String getError() {
            return error;
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
