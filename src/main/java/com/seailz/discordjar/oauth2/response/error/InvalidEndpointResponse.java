package com.seailz.discordjar.oauth2.response.error;

import com.seailz.discordjar.oauth2.response.Response;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Invalid Endpoint", value = org.springframework.http.HttpStatus.NOT_FOUND)
public class InvalidEndpointResponse implements Response {
    private final int code = 404;
    private final String message = "Invalid Endpoint";

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
