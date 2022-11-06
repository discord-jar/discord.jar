package com.seailz.javadiscordwrapper.model.embed;

import com.seailz.javadiscordwrapper.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record EmbedProvider(
        String name,
        String url
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("url", url);
        return obj;
    }

    @NonNull
    public static EmbedProvider decompile(JSONObject obj) {
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
