package com.seailz.discordjar.voice.model;

import com.seailz.discordjar.core.Compilerable;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public record VoiceServerUpdate(
        String token,
        String guildId,
        String endpoint
) implements Compilerable {


    public static @NotNull VoiceServerUpdate decompile(@NotNull JSONObject obj) {
        String token = obj.has("token") ? obj.getString("token") : null;
        String guildId = obj.has("guild_id") ? obj.getString("guild_id") : null;
        String endpoint = obj.has("endpoint") ? obj.getString("endpoint") : null;
        return new VoiceServerUpdate(token, guildId, endpoint);
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("guild_id", guildId);
        obj.put("endpoint", endpoint);
        return obj;
    }
}
