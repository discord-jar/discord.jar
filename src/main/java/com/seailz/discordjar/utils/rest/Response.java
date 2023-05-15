package com.seailz.discordjar.utils.rest;

import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Response from Discord's API.
 * @see DiscordRequest
 * @author Seailz
 * @since 1.0.0
 */
public class Response<T> {

    private final CompletableFuture<T> responseFuture = new CompletableFuture<>();
    private final CompletableFuture<Error> errorFuture = new CompletableFuture<>();
    private boolean throwOnError = false;

    public Response() {}

    public T awaitCompleted() {
        return responseFuture.join();
    }

    public Error awaitError() {
        return errorFuture.join();
    }

    public Error getError() {
        return errorFuture.getNow(null);
    }

    public T getResponse() {
        return responseFuture.getNow(null);
    }

    /**
     * Tells the class to throw a runtime exception if an error is received.
     */
    public Response<T> throwOnError() {
        throwOnError = true;
        return this;
    }

    public Response<T> complete(T response) {
        responseFuture.complete(response);
        return this;
    }

    public Response<T> completeError(Error error) {
        errorFuture.complete(error);
        if (throwOnError) {
            throw new DiscordResponseError(error);
        }
        return this;
    }

    public Response<T> onCompletion(Consumer<T> consumer) {
        responseFuture.thenAccept(consumer);
        return this;
    }

    public Response<T> onError(Consumer<Error> consumer) {
        errorFuture.thenAccept(consumer);
        return this;
    }

    public Response<T> completeAsync(Supplier<T> response) {
        CompletableFuture.supplyAsync(response).thenAccept(this::complete);
        return this;
    }

    public static class DiscordResponseError extends RuntimeException {
        private final Error error;

        public DiscordResponseError(Error error) {
            super(error.getMessage());
            this.error = error;
        }

        public Error getError() {
            return error;
        }
    }

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