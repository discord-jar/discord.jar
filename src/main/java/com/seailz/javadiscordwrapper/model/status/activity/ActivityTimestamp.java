package com.seailz.javadiscordwrapper.model.status.activity;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;

/**
 * Represents the timestamps of an activity
 * @param start
 * @param end
 *
 * @author Seailz
 * @since  1.0
 * @see    Activity
 */
public record ActivityTimestamp(
        int start,
        int end
) implements Compilerable {

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("start", start)
                .put("end", end);
    }

    public static ActivityTimestamp decompile(JSONObject obj) {
        return new ActivityTimestamp(
                obj.getInt("start"),
                obj.getInt("end")
        );
    }
}
