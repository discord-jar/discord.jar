package com.seailz.discordjar.rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public record DiscordResponse(
        int code,
        JSONObject body,
        HashMap<String, String> headers,
        JSONArray arr
) {
}
