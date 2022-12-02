package com.seailz.discordjv.model.channel.audio.internal;

import com.seailz.discordjv.model.channel.audio.VoiceRegion;

/**
 * Impl of {@link VoiceRegion}
 */
public class VoiceRegionImpl implements VoiceRegion {

    private final String id;
    private final String name;
    private final boolean optimal;
    private final boolean deprecated;
    private final boolean custom;

    public VoiceRegionImpl(String id, String name, boolean optimal, boolean deprecated, boolean custom) {
        this.id = id;
        this.name = name;
        this.optimal = optimal;
        this.deprecated = deprecated;
        this.custom = custom;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean optimal() {
        return optimal;
    }

    @Override
    public boolean deprecated() {
        return deprecated;
    }

    @Override
    public boolean custom() {
        return custom;
    }
}
