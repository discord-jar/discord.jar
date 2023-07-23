package com.seailz.discordjar.model.emoji.sticker;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.Snowflake;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

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
) implements Compilerable, Snowflake {


    @Override
    public SJSONObject compile() {
        SJSONArray stickers = new SJSONArray();
        this.stickers.forEach(sticker -> stickers.put(sticker.compile()));

        return new SJSONObject()
                .put("id", id)
                .put("stickers", stickers)
                .put("name", name)
                .put("sku_id", skuId)
                .put("cover_sticker_id", coverStickerId)
                .put("description", description)
                .put("banner_id", bannerId);
    }

    public static StickerPack decompile(SJSONObject json, DiscordJar discordJar) {
        List<Sticker> stickers = new ArrayList<>();
        json.getJSONArray("stickers").forEach(object -> stickers.add(Sticker.decompile((SJSONObject) object, discordJar)));

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

    public static List<StickerPack> decompileList(SJSONObject json, DiscordJar discordJar) {
        SJSONArray arr = json.getJSONArray("sticker_packs");
        List<StickerPack> packs = new ArrayList<>();
        arr.forEach(object -> packs.add(decompile((SJSONObject) object, discordJar)));
        return packs;
    }
}
