package com.seailz.discordjar.model.channel.utils;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;

public record ChannelMention(
        String id,
        String guildId,
        ChannelType type,
        String name
) implements Compilerable {

    public static ChannelMention decompile(SJSONObject obj) {
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
    public SJSONObject compile() {
        SJSONObject obj = new SJSONObject();
        obj.put("id", id);
        obj.put("guild_id", guildId);
        obj.put("type", type);
        obj.put("name", name);
        return obj;
    }
}
