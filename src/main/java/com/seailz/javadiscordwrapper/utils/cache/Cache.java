package com.seailz.javadiscordwrapper.utils.cache;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.guild.Guild;
import com.seailz.javadiscordwrapper.utils.URLS;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordRequest;
import com.seailz.javadiscordwrapper.utils.discordapi.DiscordResponse;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final DiscordJv discordJv;
    private final Class<T> clazz;
    private final DiscordRequest discordRequest;

    public Cache(DiscordJv discordJv, Class<T> clazz, DiscordRequest request) {
        this.discordJv = discordJv;
        this.clazz = clazz;
        this.discordRequest = request;
    }

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
    public T getById(String id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        AtomicReference<Object> returnObject = new AtomicReference<>();
        cache.forEach(t -> {
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

        if (returnObject.get() == null) {
            // request from discord
            DiscordResponse response = new DiscordRequest(
                    discordRequest.body(), discordRequest.headers(), discordRequest.url().replaceAll("%s", id), discordJv, discordRequest.url(), RequestMethod.GET
            ).invoke();
            Method decompile;
            try {
                decompile = clazz.getMethod("decompile", JSONObject.class, DiscordJv.class);
            } catch (NoSuchMethodException e) {
                decompile = clazz.getMethod("decompile", JSONObject.class);
            }

            returnObject.set(decompile.invoke(null, response.body(), discordJv));
        }

        if (returnObject.get() != null) cache.add((T) returnObject.get());
        return returnObject.get() == null ? null : (T) returnObject.get();
    }
}
