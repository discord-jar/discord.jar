package com.seailz.discordjar.utils.rest;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.utils.URLS;
import com.seailz.discordjar.utils.rest.ratelimit.Bucket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
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

    public void queueRequest(double resetAfter, Bucket bucket) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // get current epoch time
                double currentEpoch = Instant.now().toEpochMilli() * 10;
                if (currentEpoch > resetAfter) {
                    djv.removeBucket(bucket);
                    try {
                        invoke();
                    } catch (UnhandledDiscordAPIErrorException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
            }
        }).start();
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

            HttpRequest.Builder con = HttpRequest.newBuilder();
            con.uri(obj.toURI());

            boolean useBaseUrlForRateLimit = !url.contains("channels") && !url.contains("guilds");
            Bucket bucket = djv.getBucketForUrl(url);
            if (bucket != null) {
                if (bucket.getRemaining() == 0 ) {
                    if (djv.isDebug()) {
                        Logger.getLogger("RATELIMIT").info(
                                "[RATE LIMIT] Request has been rate-limited. It has been queued."
                        );
                    }
                    queueRequest(bucket.getResetAfter(), bucket);
                    return new DiscordResponse(429, null, null, null);
                }
            }

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
            System.out.println(response.headers().map().isEmpty());
            System.out.println(response.headers().map().toString());
            int responseCode = response.statusCode();
            if (djv.isDebug()) {
                System.out.println(request.method() + " " +  request.uri() + " with " + body + " returned " + responseCode + " with " + response.body());
            }
            HashMap<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, value.get(0)));

            // check headers for rate-limit
            if (response.headers().firstValue(("X-RateLimit-Bucket")).isPresent()) {
                System.out.println("Rate limit headers found!");
                String bucketId = response.headers().firstValue("X-RateLimit-Bucket").get();
                Bucket buck = djv.getBucket(bucketId);

                List<String> affectedRoutes = buck == null ? new ArrayList<>() : new ArrayList<>(buck.getAffectedRoutes());
                if (useBaseUrlForRateLimit) affectedRoutes.add(baseUrl);
                else affectedRoutes.add(url);

                djv.updateBucket(bucketId, new Bucket(
                        bucketId, Integer.parseInt(response.headers().firstValue("X-RateLimit-Remaining").get()),
                        Double.parseDouble(response.headers().firstValue(
                                "X-RateLimit-Reset"
                        ).get())
                ).setAffectedRoutes(affectedRoutes));
            }

            try {
                if (response.body().startsWith("[")) {
                    new JSONArray(response.body());
                } else {
                    new JSONObject(response.body());
                }
            } catch (JSONException err) {
                System.out.println(response.body());
            }

            if (responseCode == 429) {
                if (djv.isDebug()) {
                    Logger.getLogger("RateLimit").warning("[RATE LIMIT] Rate limit has been exceeded. Please make sure" +
                            " you are not sending too many requests.");
                }

                JSONObject body = new JSONObject(response.body());
                Bucket exceededBucket = djv.getBucket(response.headers().firstValue("X-RateLimit-Bucket").get());
                queueRequest(Double.parseDouble(response.headers().firstValue("X-RateLimit-Reset").get()), exceededBucket);

                if (body.has("retry_after")) {
                    new Thread(() -> {
                        try {
                            Thread.sleep((long) (body.getFloat("retry_after") * 1000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            invoke(contentType, auth);
                        } catch (UnhandledDiscordAPIErrorException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                } else {
                    queueRequest(Double.parseDouble(response.headers().firstValue("X-RateLimit-Reset").get()), exceededBucket);
                }
                if (body.getBoolean("global")) {
                    Logger.getLogger("RateLimit").severe(
                            "[RATE LIMIT] This seems to be a global rate limit. If you are not sending a huge amount" +
                                    " of requests unexpectedly, and your bot is in a lot of servers (100k+), contact Discord" +
                                    " developer support."
                    );
                }
                return new DiscordResponse(429, null, null, null);
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
        } catch (InterruptedException | IOException | URISyntaxException e) {
            // attempt gateway reconnect
            throw new DiscordUnexpectedError(e);
        }
    }

    public class DiscordUnexpectedError extends RuntimeException {
        public DiscordUnexpectedError(Throwable throwable) {
            super(throwable);
            // attempt gateway reconnect
            djv.restartGateway();
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

            con.header("User-Agent", "discord.jar (https://github.com/discord-jar/discord.jar, 1.0.0)");
            con.header("Authorization", "Bot " + djv.getToken());
            con.header("Content-Type", "multipart/form-data; boundary=" + boundary);


            HttpRequest request = con.build();
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            int responseCode = response.statusCode();

            HashMap<String, String> headers = new HashMap<>();
            response.headers().map().forEach((key, value) -> headers.put(key, value.get(0)));

            System.out.println(response.body());

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
                try {
                    throw new DiscordAPIErrorException(
                            responseCode,
                            errorObject.getString("code"),
                            errorObject.getString("message"),
                            error.toString()
                    );
                } catch (DiscordAPIErrorException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class DiscordAPIErrorException extends Exception {
        public DiscordAPIErrorException(int code, String errorCode, String error, String body) {
            super("DiscordAPI [Error " + HttpStatus.valueOf(code) + "]: " + errorCode + " " + error + " " + body);
        }
    }

    public static class UnhandledDiscordAPIErrorException extends Exception {
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
