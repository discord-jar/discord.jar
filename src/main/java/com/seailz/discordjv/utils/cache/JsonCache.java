package com.seailz.discordjv.utils.cache;

import com.seailz.discordjv.DiscordJv;
import com.seailz.discordjv.utils.cache.impl.JsonCacheImpl;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Random;

/**
 * A cache that can be used to store JSON objects.
 * <p>
 * This cache is used to store JSON objects that are retrieved from the Discord API,
 * <br>and is used in places where unnecessary requests to the Discord API would be made.
 * <p>
 * This cache is not persistent, and will be cleared when the bot is restarted.
 * <br>This cache is not thread-safe.
 *
 * @author Seailz
 */
public interface JsonCache {

    /**
     * Returns the JSON object stored in the cache,
     * <br>or null if the cache is empty.
     */
    @Nullable
    JSONObject get();

    /**
     * Updates the cache.
     *
     * @param object The object to update the cache with
     */
    void update(JSONObject object);

    /**
     * Returns the DiscordRequest object that
     * <br>is used to retrieve a fresh copy of the
     * <br>data stored by this cache.
     * <p>
     * Used by {@link #updateFresh()} to retrieve
     * <br>the latest data.
     * <p>
     * The {@link DiscordRequest} object that is returned
     * <br>is provided by the {@link DiscordJv} instance (or another internal class).
     *
     * @return {@link DiscordRequest} object that can be invoked to return the latest
     * data relevant to this cache.
     */
    DiscordRequest howToUpdateFresh();

    /**
     * Given an interval, this method starts
     * <br>a repeating timer on that interval that
     * <br>will clear the cache.
     * <p>
     * This is to be used if it is harder
     * <br>to update the cache and a better
     * <br>option to just set up a repeating clear.
     *
     * @param interval The interval on which to invalidate the cache.
     */
    default void reset(int interval) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                invalidate();
            }
        }, "CacheInvalidate" + new Random().nextInt(999)).start();
    }

    /**
     * Updates the cached object.
     * <p>
     * This will update the object by using the Discord API,
     * to request a new version of the object.
     * <p>
     * This method should not be used very often, and is
     * <br>just here in case it is needed in the future by the library,
     * <br>or if a developer using the library wants to use it.
     *
     * @see #howToUpdateFresh()
     */
    default void updateFresh() {
        if (howToUpdateFresh() == null) return;
        update(howToUpdateFresh().invoke().body());
    }

    /**
     * Invalidates the cache.
     * Literally just sets the cached object to null.
     */
    default void invalidate() {
        update(null);
    }

    /**
     * Returns true if the cache is empty or false if it is not.
     */
    default boolean isEmpty() {
        return get() == null;
    }


    /**
     * Returns a new, empty JsonCache object.
     */
    static JsonCache newc(DiscordRequest howToUpdateFresh) {
        return new JsonCacheImpl(howToUpdateFresh);
    }

    /**
     * Returns a new JsonCache object with the specified object.
     *
     * @param object The object to store in the cache when initialized.
     */
    static JsonCache newc(JSONObject object, DiscordRequest howToUpdateFresh) {
        return new JsonCacheImpl(object, howToUpdateFresh);
    }

}
