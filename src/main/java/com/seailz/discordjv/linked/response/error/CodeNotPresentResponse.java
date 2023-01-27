package com.seailz.discordjv.linked.response.error;

import com.seailz.discordjv.linked.response.Response;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Code param not present", value = org.springframework.http.HttpStatus.NOT_FOUND)
public class CodeNotPresentResponse implements Response {
    private final int code = 404;
    private final String message = "Code parameter not present";

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}