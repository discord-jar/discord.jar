package com.seailz.javadiscordwrapper.model.status.activity;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;

/**
 * Represents a button in an activity
 * @param label The label of the button
 * @param url The url of the button
 */
public record ActivityButton(
        String label,
        String url
) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("label", label)
                .put("url", url);
    }

    public static ActivityButton decompile(JSONObject obj) {
        return new ActivityButton(
                obj.getString("label"),
                obj.getString("url")
        );
    }
}
