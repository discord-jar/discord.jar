package com.seailz.discordjar.model.interaction.data.message;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.select.SelectOption;
import com.seailz.discordjar.model.interaction.InteractionData;
import com.seailz.discordjar.model.interaction.data.ResolvedData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the data of a message component interaction
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.Interaction
 * @since 1.0
 */
public class MessageComponentInteractionData extends InteractionData {

    // The id of the component
    private final String customId;
    // The type of the component
    private final ComponentType componentType;
    // The selected options if this is a select menu
    private SelectOption.ResolvedSelectOption[] selectOptions;
    private List<String> snowflakes;
    private ResolvedData resolved;

    public MessageComponentInteractionData(String customId, ComponentType componentType, SelectOption.ResolvedSelectOption[] selectOptions, List<String> snowflakes, ResolvedData resolved) {
        this.customId = customId;
        this.componentType = componentType;
        this.selectOptions = selectOptions;
        this.snowflakes = snowflakes;
        this.resolved = resolved;
    }

    public MessageComponentInteractionData(JSONObject obj, DiscordJar discordJar) {
        this.customId = obj.getString("custom_id");
        this.componentType = ComponentType.getType(obj.getInt("component_type"));

        if (obj.has("values")) {
            if (componentType.equals(ComponentType.STRING_SELECT)) {
                this.selectOptions = new SelectOption.ResolvedSelectOption[obj.getJSONArray("values").length()];
                for (int i = 0; i < obj.getJSONArray("values").length(); i++) {
                    this.selectOptions[i] = SelectOption.ResolvedSelectOption.decompile(obj.getJSONArray("values").getString(i));
                }
            } else if (componentType.equals(ComponentType.ROLE_SELECT) || componentType.equals(ComponentType.USER_SELECT) || componentType.equals(ComponentType.CHANNEL_SELECT) || componentType.equals(ComponentType.MENTIONABLE_SELECT)) {
                this.snowflakes = new ArrayList<>();
                obj.getJSONArray("values").toList().forEach(snowflake -> snowflakes.add(snowflake.toString()));
            }
        }

        resolved = obj.has("resolved") ? ResolvedData.decompile(obj.getJSONObject("resolved"), discordJar) : null;
    }

    public String customId() {
        return customId;
    }

    public ComponentType componentType() {
        return componentType;
    }

    public SelectOption.ResolvedSelectOption[] selectOptions() {
        return selectOptions;
    }

    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("custom_id", customId);
        obj.put("component_type", componentType.toString().toLowerCase());
        if (selectOptions != null && componentType.equals(ComponentType.STRING_SELECT)) {
            JSONArray arr = new JSONArray();
            for (SelectOption.ResolvedSelectOption option : selectOptions) {

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

    public ResolvedData resolved() {
        return resolved;
    }

}
