package com.seailz.discordjar.model.status;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.status.activity.Activity;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents the status of a user or bot
 *
 * @param since      The unix time (in milliseconds) of when the client went idle, or null if the client is not idle
 * @param activities The user's current activities
 * @param status     The user's status
 * @param afk        Whether or not the user is afk
 * @author Seailz
 * @see Activity
 * @since 1.0
 */
public record Status(
        int since,
        Activity[] activities,
        StatusType status,
        boolean afk
) implements Compilerable {

    public Status(Activity activity, StatusType status) {
        this(0, new Activity[]{activity}, status, false);
    }

    public static Status of(Activity activity, StatusType status) {
        return new Status(activity, status);
    }

    public static Status decompile(JSONObject obj, DiscordJar discordjv) {
        int since;
        Activity[] activities;
        StatusType status;
        boolean afk;

        try {
            since = obj.getInt("since");
        } catch (Exception e) {
            since = 0;
        }

        try {
            activities = new Activity[obj.getJSONArray("activities").length()];
            for (int i = 0; i < activities.length; i++) {
                activities[i] = Activity.decompile(obj.getJSONArray("activities").getJSONObject(i), discordjv);
            }
        } catch (Exception e) {
            activities = new Activity[0];
        }

        try {
            status = StatusType.valueOf(obj.getString("status"));
        } catch (Exception e) {
            status = StatusType.ONLINE;
        }

        try {
            afk = obj.getBoolean("afk");
        } catch (Exception e) {
            afk = false;
        }

        return new Status(since, activities, status, afk);
    }

    @Override
    public JSONObject compile() {
        JSONArray activities = new JSONArray();
        for (Activity activity : this.activities) {
            activities.put(activity.compile());
        }

        return new JSONObject()
                .put("since", since == 0 ? JSONObject.NULL : since)
                .put("activities", activities)
                .put("status", status.getCode())
                .put("afk", afk);
    }
}
