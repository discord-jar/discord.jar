package com.seailz.javadiscordwrapper.utils.cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Cache object used to store objects in memory
 * @param <T> The type of object to store
 *
 * @author Seailz
 * @since 1.0
 * @see    com.seailz.javadiscordwrapper.DiscordJv
 */
public class Cache<T> {

    private final List<T> cache = new ArrayList<>();

    /**
     * Adds an object to the cache
     * @param t The object to add
     */
    public void add(T t) {
        cache.add(t);
    }

    /**
     * Removes an item from the cache
     * @param t The item to remove
     */
    public void remove(T t) {
        cache.remove(t);
    }

    /**
     * Returns the entire cache
     */
    public List<T> getCache() {
        return cache;
    }

    /**
     * Gets an item from the cache
     * @param id The id of the item to get
     * @return The item
     */
    public T getById(String id) {
        AtomicReference<Object> returnObject = new AtomicReference<>();
        cache.forEach(t -> {
            Class<?> clazz = t.getClass();
            String itemId;

            for (Method method : clazz.getMethods()) {
                if (method.getName().equals("id")) {
                    try {
                        itemId = (String) method.invoke(t);
                        if (Objects.equals(itemId, id))
                            returnObject.set(t);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return returnObject.get() == null ? null : (T) returnObject.get();
    }
}
