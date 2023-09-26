package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;

/**
 * Represents the timestamps of an activity
 *
 * @param start
 * @param end
 * @author Seailz
 * @see Activity
 * @since 1.0
 */
public record ActivityTimestamp(
        int start,
        int end
) implements Compilerable {

    public static ActivityTimestamp decompile(JSONObject obj) {
        return new ActivityTimestamp(
                obj.getInt("start"),
                obj.getInt("end")
        );
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("start", start)
                .put("end", end);
    }
}
