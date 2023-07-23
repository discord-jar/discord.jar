package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;

/**
 * Represents a button in an activity
 *
 * @param label The label of the button
 * @param url   The url of the button
 */
public record ActivityButton(
        String label,
        String url
) implements Compilerable {

    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("label", label)
                .put("url", url);
    }

    public static ActivityButton decompile(SJSONObject obj) {
        return new ActivityButton(
                obj.getString("label"),
                obj.getString("url")
        );
    }
}
