package com.seailz.discordjar.model.component.select.entity;

import com.seailz.discordjar.model.component.ActionComponent;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.select.AutoPopulatedSelect;
import com.seailz.discordjar.model.component.select.SelectMenu;
import com.seailz.discordjar.utils.Mentionable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a {@link com.seailz.discordjar.utils.Mentionable} select menu ({@link com.seailz.discordjar.model.role.Role} & {@link com.seailz.discordjar.model.user.User})
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.select.SelectMenu
 * @since 1.0
 */
public class MentionableSelectMenu implements SelectMenu {

    private String customId;
    private String placeholder;
    private int minValues;
    private int maxValues;
    private boolean disabled;
    private HashMap<MentionableType, String> defaultValues;

    private JSONObject raw;

    /**
     * Creates a new mentionable select menu
     *
     * @param customId The custom id of the select menu
     */
    public MentionableSelectMenu(String customId) {
        this.customId = customId;
    }

    /**
     * Creates a new mentionable select menu
     *
     * @param customId    The custom id of the select menu
     * @param placeholder The placeholder of the select menu
     * @param minValues   The minimum amount of values that can be selected
     * @param maxValues   The maximum amount of values that can be selected
     */
    public MentionableSelectMenu(String customId, String placeholder, int minValues, int maxValues, boolean disabled, JSONObject raw, HashMap<MentionableType, String> defaultValues) {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.disabled = disabled;
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

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public void setMinValues(int minValues) {
        this.minValues = minValues;
    }

    public void setMaxValues(int maxValues) {
        this.maxValues = maxValues;
    }

    @Override
    public ActionComponent setDisabled(boolean disabled) {
        return new MentionableSelectMenu(
                customId, placeholder, minValues, maxValues, disabled, raw, defaultValues
        );
    }

    @Override
    public ComponentType type() {
        return ComponentType.MENTIONABLE_SELECT;
    }


    public HashMap<MentionableType, String> defaultValues() {
        return defaultValues;
    }


    public MentionableSelectMenu setDefaultValues(HashMap<MentionableType, String> defaultValues) {
        if (this.defaultValues == null)
            this.defaultValues = new HashMap<>();
        this.defaultValues = defaultValues;
        return this;
    }

    public MentionableSelectMenu addDefaultValue(MentionableType type, String defaultValue) {
        if (this.defaultValues == null)
            this.defaultValues = new HashMap<>();
        this.defaultValues.put(type, defaultValue);
        return this;
    }


    @Override
    public boolean isSelect() {
        return true;
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
        if (disabled) obj.put("disabled", true);
        if (defaultValues != null && !defaultValues.isEmpty()) {
            JSONArray values = new JSONArray();
            defaultValues.forEach((type, value) -> values.put(new JSONObject().put("id", value).put("type", type.getId())));
            obj.put("default_values", values);
        }
        return obj;
    }

    public static MentionableSelectMenu decompile(JSONObject json) {
        String customId = json.has("custom_id") ? json.getString("custom_id") : null;
        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        int minValues = json.has("min_values") ? json.getInt("min_values") : 0;
        int maxValues = json.has("max_values") ? json.getInt("max_values") : 25;
        boolean disabled = json.has("disabled") && json.getBoolean("disabled");

        HashMap<MentionableType, String> defaultValues = new HashMap<>();

        if (json.has("default_values")) {
            JSONArray values = json.getJSONArray("default_values");
            for (int i = 0; i < values.length(); i++) {
                JSONObject value = values.getJSONObject(i);
                defaultValues.put(MentionableType.fromId(value.getString("type")), value.getString("id"));
            }
        }

        return new MentionableSelectMenu(customId, placeholder, minValues, maxValues, disabled, json, defaultValues);
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
