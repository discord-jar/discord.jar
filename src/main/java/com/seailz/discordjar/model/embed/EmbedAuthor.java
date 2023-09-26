package com.seailz.discordjar.model.embed;

import com.seailz.discordjar.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record EmbedAuthor(
        String name,
        String url,
        String iconUrl,
        String proxyIconUrl
) implements Compilerable {

    @NonNull
    public static EmbedAuthor decompile(JSONObject obj) {
        String name;
        String url;
        String iconUrl;
        String proxyIconUrl;

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            url = obj.getString("url");
        } catch (Exception e) {
            url = null;
        }

        try {
            iconUrl = obj.getString("icon_url");
        } catch (Exception e) {
            iconUrl = null;
        }

        try {
            proxyIconUrl = obj.getString("proxy_icon_url");
        } catch (Exception e) {
            proxyIconUrl = null;
        }
        return new EmbedAuthor(name, url, iconUrl, proxyIconUrl);
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("url", url);
        obj.put("icon_url", iconUrl);
        obj.put("proxy_icon_url", proxyIconUrl);
        return obj;
    }
}
