package com.seailz.discordjar.model.component;

import com.seailz.discordjar.model.component.select.SelectMenu;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a component that is used to layout other {@link RawComponent RawComponents}, such as {@link SelectMenu SelectMenus}
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.DisplayComponent
 * @since 1.0
 */
public class ActionRow implements DisplayComponent {

    private List<RawComponent> components;
    private JSONObject raw;

    @NotNull
    public static ActionRow of(List<RawComponent> components) {
        ActionRow row = new ActionRow();
        row.components = components;
        return row;
    }

    @NotNull
    public static ActionRow of(RawComponent... components) {
        ActionRow row = new ActionRow();
        row.components = List.of(components);
        return row;
    }

    @NotNull
    public static ActionRow decompile(JSONObject obj) {
        ActionRow row = new ActionRow();
        List<RawComponent> comp = new ArrayList<>();

        obj.getJSONArray("components").forEach(c -> {
            JSONObject component = (JSONObject) c;
            comp.add(RawComponent.unknown(component));
        });
        row.raw = obj;
        row.components = comp;
        return row;
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("type", type().getCode());

        List<Component> components = new ArrayList<>(components());
        obj.put("components", Component.compileList(components));
        return obj;
    }

    @Override
    public JSONObject raw() {
        return raw;
    }

    @Override
    public ComponentType type() {
        return ComponentType.ACTION_ROW;
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public @NotNull List<RawComponent> components() {
        return components;
    }
}
