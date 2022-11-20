package com.seailz.javadiscordwrapper.model.emoji;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/**
 * Represents a reaction to a message
 * @param count The amount of times this reaction has been used
 * @param me Whether the current user has used this reaction
 * @param emoji The emoji used in the reaction
 */
public record Reaction(
        int count,
        boolean me,
        Emoji emoji
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("count", count);
        obj.put("me", me);
        obj.put("emoji", emoji.compile());
        return obj;
    }

    @NonNull
    public static Reaction decompile(JSONObject obj, DiscordJv discordJv) {
        int count;
        boolean me;
        Emoji emoji;

        try {
            count = obj.getInt("count");
        } catch (Exception e) {
            count = 0;
        }

        try {
            me = obj.getBoolean("me");
        } catch (Exception e) {
            me = false;
        }

        try {
            emoji = Emoji.decompile(obj.getJSONObject("emoji"), discordJv);
        } catch (Exception e) {
            emoji = null;
        }
        return new Reaction(count, me, emoji);
    }
}
