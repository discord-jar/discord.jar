package com.seailz.discordjar.utils.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * Simplified implementation of {@link JSONObject} that is more suited for discord.jar's needs.
 * The main point is that this class does not throw any exceptions, and instead returns null if the key is not found.
 *
 * @since 1.0
 * @author Seailz
 */
public class SJSONObject extends JSONObject {

    public SJSONObject() {
        super();
    }

    public SJSONObject(String json) {
        super(json);
    }

    public SJSONObject(Map<?, ?> m) {
        super(m);
    }

    public SJSONObject(Object obj) {
        super(obj);
    }


    @Override
    public String getString(String key) {
        Object o = get(key);
        if (!(o instanceof String)) return null;
        return (String) o;
    }

    @Override
    public int getInt(String key) {
        Object o = get(key);
        if (!(o instanceof Integer)) return 0;
        return (int) o;
    }

    @Override
    public long getLong(String key) {
        Object o = get(key);
        if (!(o instanceof Long)) return 0;
        return (long) o;
    }

    @Override
    public double getDouble(String key) {
        Object o = get(key);
        if (!(o instanceof Double)) return 0;
        return (double) o;
    }

    @Override
    public boolean getBoolean(String key) {
        Object o = get(key);
        if (!(o instanceof Boolean)) return false;
        return (boolean) o;
    }

    @Override
    public float getFloat(String key) {
        Object o = get(key);
        if (!(o instanceof Float)) return 0;
        return (float) o;
    }

    @Override
    public SJSONArray getJSONArray(String key) {
        if (!super.has(key)) return null;
        if (super.isNull(key)) return null;
        if (!(super.get(key) instanceof SJSONArray)) return null;
        return new SJSONArray(super.getJSONArray(key).toString());
    }

    @Override
    public SJSONObject getJSONObject(String key) {
        if (!super.has(key)) return null;
        if (super.isNull(key)) return null;
        if (!(super.get(key) instanceof JSONObject)) return null;
        return new SJSONObject(super.getJSONObject(key).toString());
    }

    @Override
    public Object get(String key) {
        if (!super.has(key)) return null;
        if (super.isNull(key)) return null;
        return super.get(key);
    }


    @Override
    public SJSONObject put(String key, boolean value) {
        super.put(key, value);
        return this;
    }

    @Override
    public SJSONObject put(String key, double value) {
        super.put(key, value);
        return this;
    }

    @Override
    public SJSONObject put(String key, int value) {
        super.put(key, value);
        return this;
    }

    @Override
    public SJSONObject put(String key, long value) {
        super.put(key, value);
        return this;
    }

    @Override
    public SJSONObject put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public SJSONObject put(String key, float value) {
        super.put(key, value);
        return this;
    }

    @Override
    public JSONObject putOpt(String key, Object value) throws JSONException {
        super.putOpt(key, value);
        return this;
    }

    @Override
    public JSONObject put(String key, Map<?, ?> value) throws JSONException {
        super.put(key, value);
        return this;
    }

    /**
     * Simple util that allows insertion of a list into a {@link JSONObject} with a function to convert the objects in the list.
     * <br>You could do something like return a {@link SJSONObject} from the function to make the array a list of objects.
     * <br>It really just simplifies the process of converting a list of objects into a {@link JSONArray}.
     *
     * @param key The key to insert the array into.
     * @param value The list to convert.
     * @param func The function to convert the objects in the list.
     * @return The {@link SJSONObject} with the array inserted.
     */
    public SJSONObject put(String key, Collection<?> value, Function<Object, Object> func) {
        SJSONArray array = new SJSONArray();
        for (Object o : value) {
            array.put(func.apply(o));
        }
        super.put(key, array);
        return this;
    }

    @Override
    public SJSONObject put(String key, Collection<?> value) {
        super.put(key, value);
        return this;
    }
}
