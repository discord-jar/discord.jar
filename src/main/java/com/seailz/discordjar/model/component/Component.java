package com.seailz.discordjar.model.component;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.component.select.SelectMenu;
import com.seailz.discordjar.model.interaction.modal.Modal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a component
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData
 * @since 1.0
 */
public interface Component extends Compilerable {

    /**
     * Decompiles a list of {@link Component}s from a {@link JSONArray}
     *
     * @param arr The {@link JSONArray} to decompile
     * @return A list of {@link Component}s
     */
    static List<Component> decompileList(JSONArray arr, DiscordJar discordJar) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            ComponentType type = ComponentType.getType(arr.getJSONObject(i).getInt("type"));
            Class<? extends Component> clazz = type.getClazz();
            Method method = null;
            try {
                method = clazz.getMethod("decompile", arr.getJSONObject(i).getClass());
                method.setAccessible(true);

                Component comp = (Component) method.invoke(null, arr.getJSONObject(i));
                components.add(comp);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                method = clazz.getMethod("decompile", arr.getJSONObject(i).getClass(), DiscordJar.class);
                method.setAccessible(true);

                Component comp = (Component) method.invoke(null, arr.getJSONObject(i), discordJar);
                components.add(comp);
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

    JSONObject raw();

    /**
     * The type of the component
     *
     * @return {@link ComponentType}
     */
    ComponentType type();

    /**
     * Whether the component is compatible with a {@link com.seailz.discordjar.model.message.Message}
     *
     * @return A boolean representing whether the component is compatible with a {@link com.seailz.discordjar.model.message.Message}
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
}
