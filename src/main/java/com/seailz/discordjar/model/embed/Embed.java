package com.seailz.discordjar.model.embed;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public record Embed(
        String title,
        EmbedType type,
        String description,
        String url,
        String timestamp,
        int color,
        EmbedFooter footer,
        EmbedImage image,
        EmbedImage thumbnail,
        EmbedImage video, // they use the same parameters, so this does not matter
        EmbedProvider provider,
        EmbedAuthor author,
        EmbedField[] fields
) implements Compilerable {


    @Override
    public JSONObject compile() {
        return new JSONObject()
                .put("title", title)
                .put("type", type.toString())
                .put("description", description)
                .put("url", url)
                .put("timestamp", timestamp)
                .put("color", color)
                .put("footer", footer.compile())
                .put("image", image.compile())
                .put("thumbnail", thumbnail.compile())
                .put("video", video.compile())
                .put("provider", provider.compile())
                .put("author", author.compile())
                .put("fields", fields);
    }

    @NonNull
    public static Embed decompile(JSONObject obj) {
        String title;
        EmbedType type;
        String description;
        String url;
        String timestamp;
        int color;
        EmbedFooter footer;
        EmbedImage image;
        EmbedImage thumbnail;
        EmbedImage video;
        EmbedProvider provider;
        EmbedAuthor author;
        EmbedField[] fields;

        try {
            title = obj.getString("title");
        } catch (JSONException e) {
            title = null;
        }

        try {
            type = EmbedType.valueOf(obj.getString("type").toUpperCase());
        } catch (JSONException e) {
            type = null;
        }

        try {
            description = obj.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {
            url = obj.getString("url");
        } catch (JSONException e) {
            url = null;
        }

        try {
            timestamp = obj.getString("timestamp");
        } catch (JSONException e) {
            timestamp = null;
        }

        try {
            color = obj.getInt("color");
        } catch (JSONException e) {
            color = 0;
        }

        try {
            footer = EmbedFooter.decompile(obj.getJSONObject("footer"));
        } catch (JSONException e) {
            footer = null;
        }

        try {
            image = EmbedImage.decompile(obj.getJSONObject("image"));
        } catch (JSONException e) {
            image = null;
        }

        try {
            thumbnail = EmbedImage.decompile(obj.getJSONObject("thumbnail"));
        } catch (JSONException e) {
            thumbnail = null;
        }

        try {
            video = EmbedImage.decompile(obj.getJSONObject("video"));
        } catch (JSONException e) {
            video = null;
        }

        try {
            provider = EmbedProvider.decompile(obj.getJSONObject("provider"));
        } catch (JSONException e) {
            provider = null;
        }

        try {
            author = EmbedAuthor.decompile(obj.getJSONObject("author"));
        } catch (JSONException e) {
            author = null;
        }

        try {
            JSONArray arr = obj.getJSONArray("fields");
            ArrayList<EmbedField> fields1 = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                fields1.add(EmbedField.decompile(arr.getJSONObject(i)));
            }
            fields = fields1.toArray(new EmbedField[0]);
        } catch (JSONException e) {
            fields = null;
        }

        return new Embed(title, type, description, url, timestamp, color, footer, image, thumbnail, video, provider, author, fields);
    }
}
