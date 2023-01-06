package com.seailz.discordjv.model.guild;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.emoji.Emoji;
import com.seailz.discordjv.model.emoji.sticker.Sticker;
import com.seailz.discordjv.utils.Snowflake;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an unavailable guild. These are usually created during the READY event.
 *
 * @param id                       The id of the guild
 * @param name                     The name of the guild
 * @param icon                     The icon hash of the guild
 * @param splash                   The splash hash of the guild
 * @param discoverySplash          The discovery splash hash of the guild
 * @param emojis                   The emojis of the guild
 * @param features                 The list of {@link GuildFeature} the guild has
 * @param approximateMemberCount   The approximate number of members in the guild
 * @param approximatePresenceCount The approximate number of online members in the guild
 * @param description              The description of the guild
 * @param stickers                 A list of {@link Sticker} objects that the guild has
 */
public record UnavailableGuild(
        String id,
        String name,
        String icon,
        String splash,
        String discoverySplash,
        List<Emoji> emojis,
        List<GuildFeature> features,
        int approximateMemberCount,
        int approximatePresenceCount,
        String description,
        List<Sticker> stickers
) implements Compilerable, Snowflake {

    @Override
    public JSONObject compile() {
        JSONArray emojis = new JSONArray();
        this.emojis.forEach(emoji -> emojis.put(emoji.compile()));

        JSONArray features = new JSONArray();
        this.features.forEach(feature -> features.put(feature.toString()));

        JSONArray stickers = new JSONArray();
        this.stickers.forEach(sticker -> stickers.put(sticker.compile()));
        return new JSONObject()
                .put("id", id)
                .put("name", name)
                .put("icon", icon)
                .put("splash", splash)
                .put("discovery_splash", discoverySplash)
                .put("emojis", emojis)
                .put("features", features)
                .put("approximate_member_count", approximateMemberCount)
                .put("approximate_presence_count", approximatePresenceCount)
                .put("description", description)
                .put("stickers", stickers);
    }

    @NotNull
    public static UnavailableGuild decompile(JSONObject obj, DiscordJv discordjv) {
        String id;
        String name;
        String icon;
        String splash;
        String discoverySplash;
        List<Emoji> emojis;
        List<GuildFeature> features;
        int approximateMemberCount;
        int approximatePresenceCount;
        String description;
        List<Sticker> stickers;

        try {
            id = obj.getString("id");
        } catch (JSONException e) {
            id = null;
        }

        try {
            name = obj.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            icon = obj.getString("icon");
        } catch (JSONException e) {
            icon = null;
        }

        try {
            splash = obj.getString("splash");
        } catch (JSONException e) {
            splash = null;
        }

        try {
            discoverySplash = obj.getString("discovery_splash");
        } catch (JSONException e) {
            discoverySplash = null;
        }

        try {
            List<Emoji> emojiList = new ArrayList<>();
            JSONArray emojiArray = obj.getJSONArray("emojis");

            for (int i = 0; i < emojiArray.length(); i++) {
                emojiList.add(Emoji.decompile(emojiArray.getJSONObject(i), discordjv));
            }
            emojis = emojiList;
        } catch (JSONException e) {
            emojis = null;
        }

        try {
            List<GuildFeature> featureList = new ArrayList<>();
            JSONArray featureArray = obj.getJSONArray("features");

            for (int i = 0; i < featureArray.length(); i++) {
                featureList.add(GuildFeature.valueOf(featureArray.getString(i)));
            }
            features = featureList;
        } catch (JSONException e) {
            features = null;
        }

        try {
            approximateMemberCount = obj.getInt("approximate_member_count");
        } catch (JSONException e) {
            approximateMemberCount = 0;
        }

        try {
            approximatePresenceCount = obj.getInt("approximate_presence_count");
        } catch (JSONException e) {
            approximatePresenceCount = 0;
        }

        try {
            description = obj.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {
            List<Sticker> stickerList = new ArrayList<>();
            JSONArray stickerArray = obj.getJSONArray("stickers");

            for (int i = 0; i < stickerArray.length(); i++) {
                stickerList.add(Sticker.decompile(stickerArray.getJSONObject(i), discordjv));
            }
            stickers = stickerList;
        } catch (JSONException e) {
            stickers = null;
        }

        return new UnavailableGuild(
                id,
                name,
                icon,
                splash,
                discoverySplash,
                emojis,
                features,
                approximateMemberCount,
                approximatePresenceCount,
                description,
                stickers
        );
    }
}
