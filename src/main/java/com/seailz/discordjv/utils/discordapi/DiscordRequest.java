package com.seailz.discordjv.utils.discordapi;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.URLS;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
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
    private DiscordResponse invoke(String json) {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

            if (djv.getRateLimits().containsKey(baseUrl) && djv.getRateLimits().get(baseUrl).remaining() == 0) {
                Logger.getLogger("DiscordJv").warning("[RATE LIMIT] Rate limit reached for " + baseUrl + ". Queuing request.");
                djv.getQueuedRequests().add(this);
                return new DiscordResponse(429, new JSONObject(), new HashMap<>(), null);
            }

            HttpRequest.Builder con = HttpRequest.newBuilder();

            con.uri(obj.toURI());

            if (requestMethod == RequestMethod.POST) {
                con.POST(HttpRequest.BodyPublishers.ofString(body.toString()));
            } else if (requestMethod == RequestMethod.PATCH) {
                con.method("PATCH", HttpRequest.BodyPublishers.ofString(body.toString()));
            } else if (requestMethod == RequestMethod.PUT) {
                con.method("PUT", HttpRequest.BodyPublishers.ofString(body.toString()));
            }
            else if (requestMethod == RequestMethod.DELETE) {
                con.method("DELETE", HttpRequest.BodyPublishers.ofString(body.toString()));
            } else if (requestMethod == RequestMethod.GET) {
                con.GET();
            } else {
                con.method(requestMethod.name(), HttpRequest.BodyPublishers.ofString(body.toString()));
            }

            con.header("User-Agent", "discord.jv (https://github.com/discord-jv/, 1.0.0)");
            con.header("Authorization", "Bot " + djv.getToken());
            con.header("Content-Type", "application/json");

            byte[] out = body.toString().getBytes(StandardCharsets.UTF_8);


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
                Logger logger = Logger.getLogger("DiscordJv");
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
                                "Unhandled Discord API Error. Please report this to the developer of DiscordJv." + error
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

    public DiscordResponse invoke(JSONObject body) {
        return invoke(body.toString());
    }

    public DiscordResponse invoke(JSONArray arr) {
        return invoke(arr.toString());
    }

    public DiscordResponse invoke() {
        return invoke(body.toString());
    }

    public DiscordResponse invokeWithFiles(File... files) {
        try {
            String url = URLS.BASE_URL + this.url;
            URL obj = new URL(url);

            if (djv.getRateLimits().containsKey(baseUrl) && djv.getRateLimits().get(baseUrl).remaining() == 0) {
                Logger.getLogger("DiscordJv").warning("[RATE LIMIT] Rate limit reached for " + baseUrl + ". Queuing request.");
                djv.getQueuedRequests().add(this);
                return new DiscordResponse(429, new JSONObject(), new HashMap<>(), null);
            }

            HttpRequest.Builder con = HttpRequest.newBuilder();

            con.uri(obj.toURI());

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("payload_json", null, RequestBody.create(this.body.toString().getBytes()));

            int index = 0;
            for (File f : files) {
                builder.addFormDataPart("file[" + index + "]", f.getName(), RequestBody.create(f, MediaType.parse("application/octet-stream")));
                index++;
            }

            final Buffer buffer = new Buffer();
            builder.build().writeTo(buffer);
            String body = buffer.readUtf8();
            System.out.println(body);

            if (requestMethod == RequestMethod.POST) {
                con.POST(HttpRequest.BodyPublishers.ofString(body));
            } else if (requestMethod == RequestMethod.PATCH) {
                con.method("PATCH", HttpRequest.BodyPublishers.ofString(body));
            } else if (requestMethod == RequestMethod.PUT) {
                con.method("PUT", HttpRequest.BodyPublishers.ofString(body));
            } else if (requestMethod == RequestMethod.DELETE) {
                con.method("DELETE", HttpRequest.BodyPublishers.ofString(body));
            } else if (requestMethod == RequestMethod.GET) {
                con.GET();
            } else {
                con.method(requestMethod.name(), HttpRequest.BodyPublishers.ofString(body));
            }

            con.header("User-Agent", "discord.jv (https://github.com/discord-jv/, 1.0.0)");
            con.header("Authorization", "Bot " + djv.getToken());
            con.header("Content-Type", builder.build().contentType().toString());

            System.out.println(builder.build());


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
                Logger logger = Logger.getLogger("DiscordJv");
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
                                "Unhandled Discord API Error. Please report this to the developer of DiscordJv." + error
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
}
