package com.seailz.discordjar.model.component.impl;

import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.component.RawComponent;
import com.seailz.discordjar.utils.json.SJSONObject;

/**
 * This is usually a component that will be returned by a method like {@link DisplayComponent#components()} but is not a known component type,
 * <br>As per the methods in {@link com.seailz.discordjar.model.component.RawComponent RawComponent}, this component can be transformed into a known component type like a {@link com.seailz.discordjar.model.component.button.Button Button}.
 * <p>This is purely a class to sit in between the {@link com.seailz.discordjar.model.component.RawComponent RawComponent} interface and known component types.
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.RawComponent
 * @since 1.0
 */
public class UnknownRawComponent implements RawComponent {

    private ComponentType type;
    private SJSONObject raw;

    public UnknownRawComponent(ComponentType type, SJSONObject raw) {
        this.type = type;
        this.raw = raw;
    }

    @Override
    public SJSONObject compile() {
        return raw;
    }

    @Override
    public ComponentType type() {
        return type;
    }

    @Override
    public boolean isSelect() {
        return type.isSelect();
    }

    @Override
    public SJSONObject raw() {
        return raw;
    }

    @Override
    public void setRaw(SJSONObject raw) {
        this.raw = raw;
    }

    public static UnknownRawComponent of(SJSONObject raw) {
        ComponentType type = ComponentType.getType(raw.getInt("type"));
        return new UnknownRawComponent(type, raw);
    }
}
