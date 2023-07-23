package com.seailz.discordjar.utils.rest;

import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.util.HashMap;

public record DiscordResponse(
        int code,
        SJSONObject body,
        HashMap<String, String> headers,
        SJSONArray arr
) {
}
