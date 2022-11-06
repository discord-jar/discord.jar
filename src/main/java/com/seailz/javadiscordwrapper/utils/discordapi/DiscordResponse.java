package com.seailz.javadiscordwrapper.utils.discordapi;

import org.json.JSONObject;

public record DiscordResponse(
        int code,
        JSONObject body
) {

}
