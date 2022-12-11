package com.seailz.discordjv.utils.cache;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import com.seailz.discordjv.utils.discordapi.DiscordResponse;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Cache object used to store objects in memory
 *
 * @param <T> The type of object to store
 * @author Seailz
 * @see com.seailz.discordjv.DiscordJv
 * @since 1.0
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

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cache.clear();
            }
        }).start();
    }

    /**
     * Adds an object to the cache
     *
     * @param t The object to add
     */
    public void cache(@NotNull T t)  {
        String id;
        try {
             id = (String) t.getClass().getMethod("id").invoke(t);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (T cacheMember : cache) {
            String cacheId;
            try {
                cacheId = (String) cacheMember.getClass().getMethod("id").invoke(cacheMember);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            if (cacheId.equals(id)) {
                cache.remove(cacheMember);
                break;
            }
        }
        cache.add(t);
    }

    /**
     * Removes an item from the cache
     *
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
     *
     * @param id The id of the item to get
     * @return The item
     */
    public T getById(String id) {
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
                try {
                    decompile = clazz.getMethod("decompile", JSONObject.class);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger("DiscordJv").severe("Was unable to return user from cache, please report this to discord.jv's github!");
                    throw new RuntimeException(ex);
                }
            }

            try {
                System.out.println(decompile.getParameterCount());
                returnObject.set(decompile.invoke(null, response.body(), discordJv));
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                Logger.getLogger("DiscordJv").warning("Was unable to return object from cache, attempting to remove discord.jv instance...");
                try {
                    returnObject.set(decompile.invoke(null, response.body()));
                    Logger.getLogger("discord.jv").info("Successfully retrieved object from cache!");
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger("DiscordJv").severe("Was unable to return user from cache, please report this to discord.jv's github!");
                    throw new RuntimeException(ex);
                }
            }
        }

        if (returnObject.get() != null) cache.add((T) returnObject.get());
        return returnObject.get() == null ? null : (T) returnObject.get();
    }
}
