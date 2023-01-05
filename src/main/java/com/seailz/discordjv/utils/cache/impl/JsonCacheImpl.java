package com.seailz.discordjv.utils.cache.impl;

import com.seailz.discordjv.utils.cache.JsonCache;
import com.seailz.discordjv.utils.discordapi.DiscordRequest;
import org.json.JSONObject;

/**
 * Implementation of the {@link JsonCache} interface.
 *
 * @author Seailz
 */
public class JsonCacheImpl implements JsonCache {

    private JSONObject object;
    private final DiscordRequest request;

    /**
     * Creates a new JsonCacheImpl object.
     *
     * @param object The object to store in the cache
     */
    public JsonCacheImpl(JSONObject object, DiscordRequest request) {
        this.object = object;
        this.request = request;
    }

    /**
     * Creates an empty JsonCacheImpl object.
     */
    public JsonCacheImpl(DiscordRequest request) {
        this.object = null;
        this.request = request;
    }

    @Override
    public JSONObject get() {
        return object;
    }

    @Override
    public void update(JSONObject object) {
        this.object = object;
    }

    @Override
    public DiscordRequest howToUpdateFresh() {
        return request;
    }
}
