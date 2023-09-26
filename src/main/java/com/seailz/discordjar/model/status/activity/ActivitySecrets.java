package com.seailz.discordjar.model.status.activity;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the secrets of an activity
 *
 * @param join     The secret for joining a party
 * @param spectate The secret for spectating a game
 * @param match    The secret for a specific instanced match
 * @author Seailz
 * @see Activity
 * @since 1.0
 */
public record ActivitySecrets(
        String join,
        String spectate,
        String match
) implements Compilerable {
    public static ActivitySecrets decompile(JSONObject obj) {
        String join;
        String spectate;
        String match;

        try {
            join = obj.getString("join");
        } catch (JSONException e) {
            join = null;
        }

        try {
            spectate = obj.getString("spectate");
        } catch (JSONException e) {
            spectate = null;
        }

        try {
            match = obj.getString("match");
        } catch (JSONException e) {
            match = null;
        }

        return new ActivitySecrets(join, spectate, match);
    }

    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("join", join)
                .put("spectate", spectate)
                .put("match", match);
    }
}
