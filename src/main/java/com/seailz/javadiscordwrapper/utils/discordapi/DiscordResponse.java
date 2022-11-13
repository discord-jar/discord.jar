package com.seailz.javadiscordwrapper.utils.discordapi;

import org.json.JSONObject;

import java.util.HashMap;

public record DiscordResponse(
        int code,
        JSONObject body,
        HashMap<String, String> headers
) {

}
