package com.seailz.javadiscordwrapper.utils.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Cache object used to store objects in memory
 * @param <T> The type of object to store
 */
public class Cache<T> {

    private final List<T> cache = new ArrayList<>();

    public void add(T t) {
        cache.add(t);
    }

    public void remove(T t) {
        cache.remove(t);
    }

    public List<T> getCache() {
        return cache;
    }
}
