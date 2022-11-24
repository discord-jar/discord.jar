package com.seailz.discordjv.model.component;

import com.seailz.discordjv.model.component.select.SelectMenu;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a component that is used to layout other {@link RawComponent RawComponents}, such as {@link SelectMenu SelectMenus}
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.component.DisplayComponent
 * @since 1.0
 */
public class ActionRow implements DisplayComponent {

    private List<RawComponent> components;

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("type", type().getCode());

        List<Component> components = new ArrayList<>(components());
        obj.put("components", Component.compileList(components));
        return obj;
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

        Component.decompileList(obj.getJSONArray("components")).forEach(component -> comp.add((RawComponent) component));
        return row;
    }
}
