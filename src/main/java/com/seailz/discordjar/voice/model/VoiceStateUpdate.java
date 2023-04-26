package com.seailz.discordjar.voice.model;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.guild.Member;
import org.json.JSONObject;

public record VoiceStateUpdate(
        String guildId,
        String channelId,
        String userId,
        Member member,
        String sessionId,
        boolean deaf,
        boolean mute,
        boolean selfDeaf,
        boolean selfMute,
        boolean selfStream,
        boolean selfVideo,
        boolean suppress,
        String requestToSpeakTimestamp
) implements Compilerable {


    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("guild_id", guildId);
        obj.put("channel_id", channelId);
        obj.put("user_id", userId);
        obj.put("member", member.compile());
        obj.put("session_id", sessionId);
        obj.put("deaf", deaf);
        obj.put("mute", mute);
        obj.put("self_deaf", selfDeaf);
        obj.put("self_mute", selfMute);
        obj.put("self_stream", selfStream);
        obj.put("self_video", selfVideo);
        obj.put("suppress", suppress);
        obj.put("request_to_speak_timestamp", requestToSpeakTimestamp);
        return obj;
    }

    public static VoiceStateUpdate decompile(JSONObject obj, DiscordJar djar) {
        String guildId = obj.has("guild_id") && !obj.isNull("guild_id") ? obj.getString("guild_id") : null;
        String channelId = obj.has("channel_id") && !obj.isNull("channel_id") ? obj.getString("channel_id") : null;
        String userId = obj.has("user_id") && !obj.isNull("user_id") ? obj.getString("user_id") : null;
        Member member = obj.has("member") && !obj.isNull("member") ? Member.decompile(obj.getJSONObject("member"), djar, guildId, null) : null;
        String sessionId = obj.has("session_id") && !obj.isNull("session_id") ? obj.getString("session_id") : null;
        boolean deaf = obj.has("deaf") && obj.getBoolean("deaf");
        boolean mute = obj.has("mute") && obj.getBoolean("mute");
        boolean selfDeaf = obj.has("self_deaf") && obj.getBoolean("self_deaf");
        boolean selfMute = obj.has("self_mute") && obj.getBoolean("self_mute");
        boolean selfStream = obj.has("self_stream") && obj.getBoolean("self_stream");
        boolean selfVideo = obj.has("self_video") && obj.getBoolean("self_video");
        boolean suppress = obj.has("suppress") && obj.getBoolean("suppress");
        String requestToSpeakTimestamp = obj.has("request_to_speak_timestamp") && !obj.isNull("request_to_speak_timestamp") ? obj.getString("request_to_speak_timestamp") : null;
        return new VoiceStateUpdate(guildId, channelId, userId, member, sessionId, deaf, mute, selfDeaf, selfMute, selfStream, selfVideo, suppress, requestToSpeakTimestamp);
    }

}
