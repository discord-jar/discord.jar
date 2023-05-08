package com.seailz.discordjar.utils.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public boolean completed = false;

    public List<Consumer<T>> onCompletion = new ArrayList<>();
    public List<Consumer<Error>> onError = new ArrayList<>();

    public Response() {}

    /**
     * Blocks the current thread until the response is received, or an error occurs.
     * @return Either the response or null if an error occurred.
     */
    public T awaitCompleted() {
        while (!completed) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response; // Response will be null if an error occurred
    }

    /**
     * Blocks the current thread until an error occurs, or the response is received.
     * @return Either the error or null if the response was received.
     */
    public Error awaitError() {
        while (!completed) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return error; // Error will be null if no error occurred
    }

    public Error getError() {
        return error;
    }

    public T getResponse() {
        return response;
    }

    public Response<T> complete(T response) {
        this.response = response;
        for (Consumer<T> tConsumer : onCompletion) {
            tConsumer.accept(response);
        }
        completed = true;
        return this;
    }

    public Response<T> completeError(Error error) {
        this.error = error;
        for (Consumer<Error> errorConsumer : onError) {
            errorConsumer.accept(error);
        }
        completed = true;
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
