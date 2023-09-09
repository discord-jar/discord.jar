package com.seailz.discordjar.model.emoji;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a reaction to a message
 *
 * @param count The amount of times this reaction has been used (including super-reactions)
 * @param me    Whether the current user has used this reaction
 * @param meBurst Whether the current user has super-reacted using this emoji
 * @param emoji The emoji used in the reaction
 */
public record Reaction(
        int count,
        CountDetails countDetails,
        boolean me,
        boolean meBurst,
        Emoji emoji,
        List<String> burstColors
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("count", count);
        obj.put("me", me);
        obj.put("me_burst", meBurst);
        obj.put("emoji", emoji.compile());
        if (countDetails != null) obj.put("count_details", countDetails.compile());

        JSONArray burstColors = new JSONArray();
        for (String color : this.burstColors) {
            burstColors.put(color);
        }

        obj.put("burst_colors", burstColors);

        return obj;
    }

    @NonNull
    public static Reaction decompile(JSONObject obj, DiscordJar discordJar) {
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
            emoji = Emoji.decompile(obj.getJSONObject("emoji"), discordJar);
        } catch (Exception e) {
            emoji = null;
        }

        boolean meBurst = obj.has("me_burst") && !obj.isNull("me_burst") && obj.getBoolean("me_burst");
        CountDetails details = obj.has("count_details") && !obj.isNull("count_details") ? CountDetails.decompile(obj.getJSONObject("count_details")) : null;

        List<String> burstColors = new ArrayList<>();
        if (obj.has("burst_colors") && !obj.isNull("burst_colors")) {
            for (Object o : obj.getJSONArray("burst_colors")) {
                burstColors.add((String) o);
            }
        }

        return new Reaction(count, details, me, meBurst, emoji, burstColors);
    }

    public record CountDetails(
            int burst,
            int normal
    ) implements Compilerable {

        @Override
        public JSONObject compile() {
            JSONObject obj = new JSONObject();
            obj.put("burst", burst);
            obj.put("normal", normal);
            return obj;
        }

        @NonNull
        public static CountDetails decompile(JSONObject obj) {
            int burst = obj.has("burst") && !obj.isNull("burst") ? obj.getInt("burst") : 0;
            int normal = obj.has("normal") && !obj.isNull("normal") ? obj.getInt("normal") : 0;
            return new CountDetails(burst, normal);
        }
    }
}
