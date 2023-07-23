package com.seailz.discordjar.utils.json;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;

/**
 * Similar story to {@link SJSONObject}, but for {@link JSONArray}.
 *
 * @since 1.0
 * @see SJSONObject
 * @author Seailz
 */
public class SJSONArray extends JSONArray {

    public SJSONArray() {
        super();
    }

    public SJSONArray(String json) {
        super(json);
    }

    public SJSONArray(Collection<?> collection) {
        super(collection);
    }

    public SJSONArray(Iterable<?> iter) {
        super(iter);
    }

    public SJSONArray(Object array) {
        super(array);
    }

    @Override
    public boolean getBoolean(int index) {
        Object o = get(index);
        if (!(o instanceof Boolean)) return false;
        return (boolean) o;
    }

    @Override
    public String getString(int index) {
        Object o = get(index);
        if (!(o instanceof String)) return null;
        return (String) o;
    }

    @Override
    public int getInt(int index) {
        Object o = get(index);
        if (!(o instanceof Integer)) return 0;
        return (int) o;
    }

    @Override
    public long getLong(int index) {
        Object o = get(index);
        if (!(o instanceof Long)) return 0;
        return (long) o;
    }

    @Override
    public double getDouble(int index) {
        Object o = get(index);
        if (!(o instanceof Double)) return 0;
        return (double) o;
    }

    @Override
    public float getFloat(int index) {
        Object o = get(index);
        if (!(o instanceof Float)) return 0;
        return (float) o;
    }

    @Override
    public SJSONArray getJSONArray(int index) {
        if (index >= length()) return null;
        if (super.isNull(index)) return null;
        return new SJSONArray(super.getJSONArray(index).toString());
    }

    @Override
    public SJSONObject getJSONObject(int index) {
        if (index >= length()) return null;
        if (super.isNull(index)) return null;
        return new SJSONObject(super.getJSONObject(index).toString());
    }

    @Override
    public Object get(int index) {
        if (index >= length()) return null;
        if (super.isNull(index)) return null;
        return super.get(index);
    }
}
