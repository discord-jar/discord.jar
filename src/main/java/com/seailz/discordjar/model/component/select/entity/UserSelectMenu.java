package com.seailz.discordjar.model.component.select.entity;

import com.seailz.discordjar.model.component.ActionComponent;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.select.AutoPopulatedSelect;
import com.seailz.discordjar.model.component.select.SelectMenu;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a user select menu
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.select.SelectMenu
 * @since 1.0
 */
public class UserSelectMenu implements AutoPopulatedSelect {

    private String customId;
    private String placeholder;
    private int minValues;
    private int maxValues;
    private boolean isDisabled;
    private List<String> defaultValues;

    private JSONObject raw;

    /**
     * Creates a new user select menu
     *
     * @param customId The custom id of the select menu
     */
    public UserSelectMenu(String customId) {
        this.customId = customId;
    }

    /**
     * Creates a new user select menu
     *
     * @param customId    The custom id of the select menu
     * @param placeholder The placeholder of the select menu
     * @param minValues   The minimum amount of values that can be selected
     * @param maxValues   The maximum amount of values that can be selected
     */
    public UserSelectMenu(String customId, String placeholder, int minValues, int maxValues, boolean isDisabled, JSONObject raw, List<String> defaultValues) {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.isDisabled = isDisabled;
        this.raw = raw;
        this.defaultValues = defaultValues;
    }

    @Override
    public String customId() {
        return customId;
    }

    @Override
    public String placeholder() {
        return placeholder;
    }

    @Override
    public int minValues() {
        return minValues;
    }

    @Override
    public int maxValues() {
        return maxValues;
    }

    public UserSelectMenu setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    public UserSelectMenu setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public UserSelectMenu setMinValues(int minValues) {
        this.minValues = minValues;
        return this;
    }

    public UserSelectMenu setMaxValues(int maxValues) {
        this.maxValues = maxValues;
        return this;
    }

    public UserSelectMenu setDefaultValues(List<String> defaultValues) {
        if (this.defaultValues == null)
            this.defaultValues = new ArrayList<>();
        this.defaultValues = defaultValues;
        return this;
    }

    public UserSelectMenu addDefaultValue(String... defaultValue) {
        if (this.defaultValues == null)
            this.defaultValues = new ArrayList<>();
        this.defaultValues.addAll(Arrays.asList(defaultValue));
        return this;
    }

    @Override
    public ActionComponent setDisabled(boolean disabled) {
        return new UserSelectMenu(
                customId, placeholder, minValues, maxValues, disabled, raw, defaultValues
        );
    }

    @Override
    public ComponentType type() {
        return ComponentType.USER_SELECT;
    }

    @Override
    public boolean isSelect() {
        return true;
    }

    @Override
    public List<String> defaultValues() {
        return defaultValues;
    }

    @Override
    public JSONObject compile() {
        if (minValues > maxValues)
            throw new IllegalArgumentException("Min values cannot be greater than max values");

        JSONObject obj = new JSONObject();
        obj.put("type", type().getCode());
        obj.put("custom_id", customId);
        if (placeholder != null) obj.put("placeholder", placeholder);
        if (minValues != 0) obj.put("min_values", minValues);
        if (maxValues != 0) obj.put("max_values", maxValues);
        if (isDisabled) obj.put("disabled", true);
        if (defaultValues != null && !defaultValues.isEmpty()) {
            JSONArray values = new JSONArray();
            for (String value : defaultValues) {
                values.put(new JSONObject().put("id", value).put("type", "user"));
            }
            obj.put("default_values", values);
        }
        return obj;
    }

    public static UserSelectMenu decompile(JSONObject json) {
        String customId = json.has("custom_id") ? json.getString("custom_id") : null;
        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        int minValues = json.has("min_values") ? json.getInt("min_values") : 0;
        int maxValues = json.has("max_values") ? json.getInt("max_values") : 25;
        boolean isDisabled = json.has("disabled") && json.getBoolean("disabled");
        List<String> defaultValues = new ArrayList<>();
        if (json.has("default_values")) {
            for (Object o : json.getJSONArray("default_values")) {
                defaultValues.add(o.toString());
            }
        }

        return new UserSelectMenu(customId, placeholder, minValues, maxValues, isDisabled, json, defaultValues);
    }

    @Override
    public JSONObject raw() {
        return raw;
    }

    @Override
    public void setRaw(JSONObject raw) {
          this.raw = raw;
    }
}


