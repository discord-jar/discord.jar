package com.seailz.discordjv.model.emoji.sticker;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.user.User;
import com.seailz.discordjv.utils.Snowflake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sticker
 *
 * @param id          The id of the sticker
 * @param packId      The id of the pack the sticker is from
 * @param name        The name of the sticker
 * @param description The description of the sticker
 * @param tags        The tags for the sticker
 * @param type        The {@link StickerType} of the sticker
 * @param format      The {@link StickerFormat} of the sticker
 * @param available   Whether the sticker is available
 * @param guildId     The id of the guild that owns this sticker
 * @param user        The user that uploaded the guild sticker
 * @param sortValue   The standard sticker's sort order within its pack
 */
public record Sticker(
        String id,
        String packId,
        String name,
        String description,
        String tags,
        StickerType type,
        StickerFormat format,
        boolean available,
        String guildId,
        User user,
        int sortValue
) implements Compilerable, Snowflake {


    /**
     * Creates a {@link JSONObject} from a {@link Sticker} object
     *
     * @return The compiled {@link JSONObject}
     */
    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("id", id)
                .put("pack_id", packId)
                .put("name", name)
                .put("description", description)
                .put("tags", tags)
                .put("type", type.getCode())
                .put("format", format.getCode())
                .put("available", available)
                .put("guild_id", guildId)
                .put("user", user.compile())
                .put("sort_value", sortValue);
    }

    /**
     * Creates a {@link Sticker} object from a {@link JSONObject}
     *
     * @param obj The {@link JSONObject} to create the {@link Sticker} object from
     * @return The compiled {@link Sticker} object
     */
    @NotNull
    public static Sticker decompile(JSONObject obj, DiscordJv discordJv) {
        String id;
        String packId;
        String name;
        String description;
        String tags;
        StickerType type;
        StickerFormat format;
        boolean available;
        String guildId;
        User user;
        int sortValue;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            packId = obj.getString("pack_id");
        } catch (JSONException e) {
            packId = null;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            description = obj.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {
            tags = obj.getString("tags");
        } catch (JSONException e) {
            tags = null;
        }

        try {
            type = StickerType.getStickerTypeByCode(obj.getInt("type"));
        } catch (JSONException e) {
            type = null;
        }

        try {
            format = StickerFormat.getStickerFormatByCode(obj.getInt("format"));
        } catch (JSONException e) {
            format = null;
        }

        try {
            available = obj.getBoolean("available");
        } catch (JSONException e) {
            available = false;
        }

        try {
            guildId = obj.getString("guild_id");
        } catch (JSONException e) {
            guildId = null;
        }

        try {
            user = User.decompile(obj.getJSONObject("user"), discordJv);
        } catch (JSONException e) {
            user = null;
        }

        try {
            sortValue = obj.getInt("sort_value");
        } catch (JSONException e) {
            sortValue = 0;
        }

        return new Sticker(id, packId, name, description, tags, type, format, available, guildId, user, sortValue);
    }

    public static List<Sticker> decompileList(JSONArray array, DiscordJv discordJv) {
        List<Sticker> stickers = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            stickers.add(decompile(array.getJSONObject(i), discordJv));
        }
        return stickers;
    }
}
