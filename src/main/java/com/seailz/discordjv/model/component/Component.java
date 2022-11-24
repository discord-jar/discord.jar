package com.seailz.discordjv.model.component;

import com.seailz.discordjv.core.Compilerable;
import com.seailz.discordjv.model.component.select.SelectMenu;
import com.seailz.discordjv.model.interaction.modal.Modal;
import org.json.JSONArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a component
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.interaction.data.message.MessageComponentInteractionData
 * @since 1.0
 */
public interface Component extends Compilerable {

    /**
     * The type of the component
     *
     * @return {@link ComponentType}
     */
    ComponentType type();

    /**
     * Whether the component is compatible with a {@link com.seailz.discordjv.model.message.Message}
     *
     * @return A boolean representing whether the component is compatible with a {@link com.seailz.discordjv.model.message.Message}
     */
    boolean isMessageCompatible();

    /**
     * Whether the component is compatible with a @{@link Modal}
     *
     * @return A boolean representing whether the component is compatible with a Modal
     */

    boolean isModalCompatible();

    /**
     * Whether the component is a {@link SelectMenu}
     *
     * @return A boolean representing whether the component is a select menu
     */
    boolean isSelect();

    /**
     * Decompiles a list of {@link Component}s from a {@link JSONArray}
     *
     * @param arr The {@link JSONArray} to decompile
     * @return A list of {@link Component}s
     */
    static List<Component> decompileList(JSONArray arr) {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            ComponentType type = ComponentType.getType(arr.getJSONObject(i).getInt("type"));
            Class<? extends Component> clazz = type.getClazz();
            try {
                Method method = clazz.getMethod("decompile", arr.getJSONObject(i).getClass());
                method.setAccessible(true);

                Component comp = (Component) method.invoke(null, arr.getJSONObject(i));
                components.add(comp);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return components;
    }

    static JSONArray compileList(List<Component> components) {
        JSONArray arr = new JSONArray();
        for (Component component : components)
            arr.put(component.compile());
        return arr;
    }

    static JSONArray compileModalList(List<ModalComponent> components) {
        JSONArray arr = new JSONArray();
        for (Component component : components)
            arr.put(component.compile());
        return arr;
    }
}
