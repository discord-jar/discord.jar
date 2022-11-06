package com.seailz.javadiscordwrapper.model.channel.utils;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;

public record ChannelMention(
        String id,
        String guildId,
        ChannelType type,
        String name
) implements Compilerable {

    public static ChannelMention decompile(JSONObject obj) {
        String id;
        String guildId;
        ChannelType type;
        String name;

        try {
            id = obj.getString("id");
        } catch (Exception e) {
            id = null;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (Exception e) {
            guildId = null;
        }

        try {
            type = ChannelType.fromCode(obj.getInt("type"));
        } catch (Exception e) {
            type = null;
        }

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }
        return new ChannelMention(id, guildId, type, name);
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("guild_id", guildId);
        obj.put("type", type);
        obj.put("name", name);
        return obj;
    }
}
