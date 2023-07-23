package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.json.JSONException;

public record ActivityAssets(
        String largeImage,
        String largeText,
        String smallImage,
        String smallText
) implements Compilerable {


    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("large_image", largeImage)
                .put("large_text", largeText)
                .put("small_image", smallImage)
                .put("small_text", smallText);
    }

    public static ActivityAssets decompile(SJSONObject obj) {
        String largeImage;
        String largeText;
        String smallImage;
        String smallText;

        try {
            largeImage = obj.getString("large_image");
        } catch (JSONException e) {
            largeImage = null;
        }

        try {
            largeText = obj.getString("large_text");
        } catch (JSONException e) {
            largeText = null;
        }

        try {
            smallImage = obj.getString("small_image");
        } catch (JSONException e) {
            smallImage = null;
        }

        try {
            smallText = obj.getString("small_text");
        } catch (JSONException e) {
            smallText = null;
        }

        return new ActivityAssets(largeImage, largeText, smallImage, smallText);
    }
}
