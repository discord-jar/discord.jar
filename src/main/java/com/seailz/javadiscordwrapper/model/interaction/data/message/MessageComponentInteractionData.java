package com.seailz.javadiscordwrapper.model.interaction.data.message;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.model.component.ComponentType;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import com.seailz.javadiscordwrapper.model.role.Role;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> snowflakes;

    public MessageComponentInteractionData(String customId, ComponentType componentType, SelectOption[] selectOptions, List<String> snowflakes) {
        this.customId = customId;
        this.componentType = componentType;
        this.selectOptions = selectOptions;
        this.snowflakes = snowflakes;
    }

    public MessageComponentInteractionData(JSONObject obj, DiscordJv discordJv) {
        this.customId = obj.getString("custom_id");
        this.componentType = ComponentType.getType(obj.getInt("component_type"));

        if (obj.has("values")) {
            if (componentType.equals(ComponentType.STRING_SELECT)) {
                this.selectOptions = new SelectOption[obj.getJSONArray("values").length()];
                for (int i = 0; i < obj.getJSONArray("values").length(); i++) {
                    this.selectOptions[i] = SelectOption.decompile(obj.getJSONArray("values").getJSONObject(i), discordJv);
                }
            } else if (componentType.equals(ComponentType.ROLE_SELECT) || componentType.equals(ComponentType.USER_SELECT) || componentType.equals(ComponentType.CHANNEL_SELECT) || componentType.equals(ComponentType.MENTIONABLE_SELECT)) {
                this.snowflakes = new ArrayList<>();
                obj.getJSONArray("values").toList().forEach(snowflake -> snowflakes.add(snowflake.toString()));
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
        if (selectOptions != null && componentType.equals(ComponentType.STRING_SELECT)) {
            JSONArray arr = new JSONArray();
            for (SelectOption option : selectOptions) {
                arr.put(option.compile());
            }
            obj.put("values", arr);
        }

        if (snowflakes != null) {
            if (componentType.equals(ComponentType.ROLE_SELECT) || componentType.equals(ComponentType.CHANNEL_SELECT) || componentType.equals(ComponentType.USER_SELECT) || componentType.equals(ComponentType.MENTIONABLE_SELECT)) {
                JSONArray arr = new JSONArray();
                snowflakes.forEach(arr::put);
                obj.put("values", arr);
            }
        }
        return obj;
    }

    public List<String> snowflakes() {
        return snowflakes;
    }

}
