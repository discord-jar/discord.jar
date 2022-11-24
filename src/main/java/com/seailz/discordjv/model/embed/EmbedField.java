package com.seailz.discordjv.model.embed;

import com.seailz.discordjv.core.Compilerable;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

public record EmbedField(
        String name,
        String value,
        boolean inline
) implements Compilerable {

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("value", value);
        obj.put("inline", inline);
        return obj;
    }

    @NonNull
    public static EmbedField decompile(JSONObject obj) {
        String name;
        String value;
        boolean inline;

        try {
            name = obj.getString("name");
        } catch (Exception e) {
            name = null;
        }

        try {
            value = obj.getString("value");
        } catch (Exception e) {
            value = null;
        }

        try {
            inline = obj.getBoolean("inline");
        } catch (Exception e) {
            inline = false;
        }
        return new EmbedField(name, value, inline);
    }

}
