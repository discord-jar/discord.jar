package com.seailz.discordjar.utils.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Response from Discord's API. This is similar to a {@link java.util.concurrent.CompletableFuture CompletableFuture}.
 * @see DiscordRequest
 * @author Seailz
 * @since 1.0.0
 */
public class Response<T> {

    public T response;
    public Error error;

    public List<Consumer<T>> onCompletion = new ArrayList<>();
    public List<Consumer<Error>> onError = new ArrayList<>();

    public Response() {}

    public Response<T> complete(T response) {
        this.response = response;
        for (Consumer<T> tConsumer : onCompletion) {
            tConsumer.accept(response);
        }
        return this;
    }

    public Response<T> completeError(Error error) {
        this.error = error;
        return this;
    }

    public Response<T> onCompletion(Consumer<T> consumer) {
        onCompletion.add(consumer);
        return this;
    }

    public Response<T> onError(Consumer<Error> consumer) {
        onError.add(consumer);
        return this;
    }

    public Response<T> completeAsync(Supplier<T> response) {
        new Thread(() -> {
            complete(response.get());
        });
        return this;
    }

    /**
     * Represents an error received from the Discord API.
     * <br>TODO: Convert this to an enum
     */
    public static class Error {
        private int code;
        private String message;
        private JSONObject errors;

        public Error(int code, String message, JSONObject errors) {
            this.code = code;
            this.message = message;
            this.errors = errors;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public JSONObject getErrors() {
            return errors;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", errors=" + errors +
                    '}';
        }
    }
}
