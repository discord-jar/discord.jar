package com.seailz.javadiscordwrapper.model.embed;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record EmbedImage(
        String url,
        String proxyUrl,
        int height,
        int width
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("url", url);
        obj.put("proxy_url", proxyUrl);
        obj.put("height", height);
        obj.put("width", width);
        return obj;
    }

    @NonNull
    public static EmbedImage decompile(JSONObject obj) {
        String url;
        String proxyUrl;
        int height;
        int width;

        try {
            url = obj.getString("url");
        } catch (Exception e) {
            url = null;
        }

        try {
            proxyUrl = obj.getString("proxy_url");
        } catch (Exception e) {
            proxyUrl = null;
        }

        try {
            height = obj.getInt("height");
        } catch (Exception e) {
            height = 0;
        }

        try {
            width = obj.getInt("width");
        } catch (Exception e) {
            width = 0;
        }
        return new EmbedImage(url, proxyUrl, height, width);
    }

}
