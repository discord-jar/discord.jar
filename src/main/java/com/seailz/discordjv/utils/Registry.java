package com.seailz.discordjv.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for registries.
 * <p>
 * Note that classes extending this class should only be instantiated once.
 * <br>Don't instantiate this class directly.
 *
 * @param <T> The type of object to be registered.
 * @author Seailz
 * @since 1.0
 */
public class Registry<T> {

    private final List<T> registry = new ArrayList<>();

    /**
     * Registers an object to the registry.
     *
     * @param object The object to register.
     */
    public void register(T object) {
        registry.add(object);
    }

    /**
     * Unregisters an object from the registry.
     *
     * @param object The object to unregister.
     */
    @SuppressWarnings("unused")
    public void unregister(T object) {
        registry.remove(object);
    }

    /**
     * Gets the registry.
     *
     * @return The registry.
     */
    public List<T> getRegistry() {
        return registry;
    }

}
