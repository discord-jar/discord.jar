package com.seailz.discordjar.cache.impl;

import com.seailz.discordjar.cache.JsonCache;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import org.json.JSONObject;

/**
 * Implementation of the {@link JsonCache} interface.
 *
 * @author Seailz
 */
public class JsonCacheImpl implements JsonCache {

    private final DiscordRequest request;
    private JSONObject object;

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
