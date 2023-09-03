package com.seailz.discordjar.utils.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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

        public Error(DiscordRequest.UnhandledDiscordAPIErrorException e) {
            this(e.getCode(), e.getMessage(), e.getBody());
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

        public class ErroredResponse {
            private String message;
            private int code;

            public ErroredResponse(String message, int code) {
                this.message = message;
                this.code = code;
            }

            public String getMessage() {
                return message;
            }

            public int getCode() {
                return code;
            }
        }

        public List<ErroredResponse> getAllErrorMessages() {
            List<ErroredResponse> errorResponses = new ArrayList<>();
            findErrorResponsesRecursive(getErrors(), errorResponses);
            return errorResponses;
        }

        private void findErrorResponsesRecursive(JSONObject jsonObject, List<ErroredResponse> errorResponses) {
            for (String key : jsonObject.keySet()) {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    findErrorResponsesRecursive((JSONObject) value, errorResponses);
                } else if (value instanceof JSONArray) {
                    findErrorResponsesInArray((JSONArray) value, errorResponses);
                } else if (key.equals("message")) {
                    int code = getCodeFromJSONObject(jsonObject);
                    errorResponses.add(new ErroredResponse(value.toString(), code));
                }
            }
        }

        private void findErrorResponsesInArray(JSONArray jsonArray, List<ErroredResponse> errorResponses) {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);
                if (value instanceof JSONObject) {
                    findErrorResponsesRecursive((JSONObject) value, errorResponses);
                } else if (value instanceof JSONArray) {
                    findErrorResponsesInArray((JSONArray) value, errorResponses);
                }
            }
        }

        private int getCodeFromJSONObject(JSONObject jsonObject) {
            int code = -1;
            if (jsonObject.has("code") && jsonObject.get("code") instanceof Integer) {
                code = jsonObject.getInt("code");
            }
            return code;
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