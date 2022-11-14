package com.seailz.javadiscordwrapper.model.interaction.data.message;

import com.seailz.javadiscordwrapper.model.component.ComponentType;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents the data of a message component interaction
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.interaction.Interaction
 */
public class MessageComponentInteractionData extends InteractionData {

    // The id of the component
    private String customId;
    // The type of the component
    private ComponentType componentType;
    // The selected options if this is a select menu
    private SelectOption[] selectOptions;

    public MessageComponentInteractionData(String customId, ComponentType componentType, SelectOption[] selectOptions) {
        this.customId = customId;
        this.componentType = componentType;
        this.selectOptions = selectOptions;
    }

    public MessageComponentInteractionData(JSONObject obj) {
        this.customId = obj.getString("custom_id");
        this.componentType = ComponentType.valueOf(obj.getString("component_type").toUpperCase());
        if (obj.has("values")) {
            this.selectOptions = new SelectOption[obj.getJSONArray("values").length()];
            for (int i = 0; i < obj.getJSONArray("values").length(); i++) {
                this.selectOptions[i] = SelectOption.decompile(obj.getJSONArray("values").getJSONObject(i));
            }
        }
    }

    public String customId() {
        return customId;
    }

    public ComponentType componentType() {
        return componentType;
    }

    public SelectOption[] selectOptions() {
        return selectOptions;
    }

    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("custom_id", customId);
        obj.put("component_type", componentType.toString().toLowerCase());
        if (selectOptions != null) {
            JSONArray arr = new JSONArray();
            for (SelectOption option : selectOptions) {
                arr.put(option.compile());
            }
            obj.put("values", arr);
        }
        return obj;
    }

}
