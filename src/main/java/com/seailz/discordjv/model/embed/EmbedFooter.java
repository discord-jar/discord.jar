package com.seailz.discordjv.model.embed;

import com.seailz.discordjv.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record EmbedFooter(
        String text,
        String iconUrl,
        String proxyIconUrl
) implements Compilerable {


    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("text", text);
        obj.put("icon_url", iconUrl);
        obj.put("proxy_icon_url", proxyIconUrl);
        return obj;
    }

    @NonNull
    public static EmbedFooter decompile(JSONObject obj) {
        String text;
        String iconUrl;
        String proxyIconUrl;

        try {
            text = obj.getString("text");
        } catch (Exception e) {
            text = null;
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
        return new EmbedFooter(text, iconUrl, proxyIconUrl);
    }
}
