package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;

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

    public static ActivityButton decompile(JSONObject obj) {
        return new ActivityButton(
                obj.getString("label"),
                obj.getString("url")
        );
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("label", label)
                .put("url", url);
    }
}
