package com.seailz.javadiscordwrapper.model.emoji.sticker;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public record StickerPack(
        String id,
        List<Sticker> stickers,
        String name,
        String skuId,
        String coverStickerId,
        String description,
        String bannerId
) implements Compilerable {


    @Override
    public JSONObject compile() {
        JSONArray stickers = new JSONArray();
        this.stickers.forEach(sticker -> stickers.put(sticker.compile()));

        return new JSONObject()
                .put("id", id)
                .put("stickers", stickers)
                .put("name", name)
                .put("sku_id", skuId)
                .put("cover_sticker_id", coverStickerId)
                .put("description", description)
                .put("banner_id", bannerId);
    }

    public static StickerPack decompile(JSONObject json, DiscordJv discordJv) {
        List<Sticker> stickers = new ArrayList<>();
        json.getJSONArray("stickers").forEach(object -> stickers.add(Sticker.decompile((JSONObject) object, discordJv)));

        return new StickerPack(
                json.getString("id"),
                stickers,
                json.getString("name"),
                json.getString("sku_id"),
                json.has("cover_sticker_id") ? json.getString("cover_sticker_id") : null,
                json.getString("description"),
                json.has("banner_id") ? json.getString("banner_id") : null
        );
    }
}
