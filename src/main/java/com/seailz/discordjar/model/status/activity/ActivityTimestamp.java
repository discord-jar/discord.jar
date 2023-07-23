package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;

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

    @Override
    public SJSONObject compile() {
        return new SJSONObject()
                .put("start", start)
                .put("end", end);
    }

    public static ActivityTimestamp decompile(SJSONObject obj) {
        return new ActivityTimestamp(
                obj.getInt("start"),
                obj.getInt("end")
        );
    }
}
