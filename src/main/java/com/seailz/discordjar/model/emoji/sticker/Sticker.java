package com.seailz.discordjar.model.emoji.sticker;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.user.User;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.model.JSONProp;
import com.seailz.discordjar.utils.model.Model;
import com.seailz.discordjar.utils.model.ModelDecoder;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sticker
 */
public class Sticker implements Model, Snowflake {

    @JSONProp("id")
    private String id;
    @JSONProp("pack_id")
    private String packId;
    @JSONProp("name")
    private String name;
    @JSONProp("description")
    private String description;
    @JSONProp("tags")
    private String tags;
    @JSONProp("type")
    private StickerType type;
    @JSONProp("format_type")
    private StickerFormat format;
    @JSONProp("available")
    private boolean available;
    @JSONProp("guild_id")
    private String guildId;
    @JSONProp("user")
    private User user;
    @JSONProp("sort_value")
    private int sortValue;

    private Sticker() {}

    public String id() {
        return id;
    }

    public String packId() {
        return packId;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public String tags() {
        return tags;
    }

    public StickerType type() {
        return type;
    }

    public StickerFormat format() {
        return format;
    }

    public boolean available() {
        return available;
    }

    public String guildId() {
        return guildId;
    }

    public User user() {
        return user;
    }

    public int sortValue() {
        return sortValue;
    }

    public static List<Sticker> decompileList(JSONArray array, DiscordJar discordJar) {
        List<Sticker> stickers = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            stickers.add((Sticker) ModelDecoder.decodeObject(array.getJSONObject(i), Sticker.class, discordJar));
        }
        return stickers;
    }
}
