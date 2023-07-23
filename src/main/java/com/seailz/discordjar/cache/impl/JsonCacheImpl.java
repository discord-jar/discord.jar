package com.seailz.discordjar.cache.impl;

import com.seailz.discordjar.cache.JsonCache;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.utils.json.SJSONObject;

/**
 * Implementation of the {@link JsonCache} interface.
 *
 * @author Seailz
 */
public class JsonCacheImpl implements JsonCache {

    private SJSONObject object;
    private final DiscordRequest request;

    /**
     * Creates a new JsonCacheImpl object.
     *
     * @param object The object to store in the cache
     */
    public JsonCacheImpl(SJSONObject object, DiscordRequest request) {
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
    public SJSONObject get() {
        return object;
    }

    @Override
    public void update(SJSONObject object) {
        this.object = object;
    }

    @Override
    public DiscordRequest howToUpdateFresh() {
        return request;
    }
}
