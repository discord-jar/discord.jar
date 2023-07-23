package com.seailz.discordjar.model.embed;

import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.utils.json.SJSONObject;
import org.springframework.lang.NonNull;

public record EmbedProvider(
        String name,
        String url
) implements Compilerable {

    @Override
    public SJSONObject compile() {
        SJSONObject obj = new SJSONObject();
        obj.put("name", name);
        obj.put("url", url);
        return obj;
    }

    @NonNull
    public static EmbedProvider decompile(SJSONObject obj) {
        String name;
        String url;

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
        return new EmbedProvider(name, url);
    }
}
