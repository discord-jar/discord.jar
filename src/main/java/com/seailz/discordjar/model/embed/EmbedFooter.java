package com.seailz.discordjar.model.embed;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.springframework.lang.NonNull;

public record EmbedFooter(
        String text,
        String iconUrl,
        String proxyIconUrl
) implements Compilerable {


    @Override
    public SJSONObject compile() {
        SJSONObject obj = new SJSONObject();
        obj.put("text", text);
        obj.put("icon_url", iconUrl);
        obj.put("proxy_icon_url", proxyIconUrl);
        return obj;
    }

    @NonNull
    public static EmbedFooter decompile(SJSONObject obj) {
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
