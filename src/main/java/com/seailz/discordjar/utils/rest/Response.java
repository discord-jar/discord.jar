package com.seailz.discordjar.utils.rest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
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

    public volatile T response;
    public volatile Error error;
    public volatile boolean completed = false;

    public List<Consumer<T>> onCompletion = new ArrayList<>();
    public List<Consumer<Error>> onError = new ArrayList<>();

    public Response() {}

    /**
     * Blocks the current thread until the response is received, or an error occurs.
     * @return Either the response or null if an error occurred.
     */
    public T awaitCompleted() {
        while(true) {
            synchronized(this) {
                if (this.completed) {
                    break;
                }
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }

        return this.response;
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
        synchronized(this) {
            this.response = response;
            Iterator var2 = this.onCompletion.iterator();

            while(var2.hasNext()) {
                Consumer<T> tConsumer = (Consumer) var2.next();
                tConsumer.accept(response);
            }

            this.completed = true;
        }
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
        }).start();
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
