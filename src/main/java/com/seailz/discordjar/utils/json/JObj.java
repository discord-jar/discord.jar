package com.seailz.discordjar.utils.json;

import org.json.JSONObject;

/**
 * Custom JSON object designed to be useful in discord.jar.
 * @author Seailz
 * @since 1.0.0
 */
public class JObj {

    private final JSONObject legacy;

    public JObj(JSONObject legacy) {
        this.legacy = legacy;
    }

    public JObj(String text) {
        this.legacy = new JSONObject(text);
    }

    public String getString(String key) {
        return checks(String.class, key) ? legacy.getString(key) : null;
    }

    public int getInt(String key) {
        return checks(Integer.class, key) ? legacy.getInt(key) : -1;
    }

    public boolean getBoolean(String key) {
        return checks(Boolean.class, key) && legacy.getBoolean(key);
    }

    public long getLong(String key) {
        return checks(Long.class, key) ? legacy.getLong(key) : -1;
    }

    public double getDouble(String key) {
        return checks(Double.class, key) ? legacy.getDouble(key) : -1;
    }

    public boolean checks(Class<?> clazz, String key) {
        if (!legacy.has(key)) return false;
        if (legacy.isNull(key)) return false;
        return clazz.isInstance(legacy.get(key));
    }

    public static JObj fromLegacy(JSONObject legacy) {
        return new JObj(legacy);
    }

}
