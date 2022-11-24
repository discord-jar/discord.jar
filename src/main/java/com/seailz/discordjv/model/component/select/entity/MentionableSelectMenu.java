package com.seailz.discordjv.model.component.select.entity;

import com.seailz.discordjv.model.component.ActionComponent;
import com.seailz.discordjv.model.component.ComponentType;
import com.seailz.discordjv.model.component.select.SelectMenu;
import org.json.JSONObject;

/**
 * Represents a {@link com.seailz.discordjv.utils.Mentionable} select menu ({@link com.seailz.discordjv.model.role.Role} & {@link com.seailz.discordjv.model.user.User})
 *
 * @author Seailz
 * @see com.seailz.discordjv.model.component.select.SelectMenu
 * @since 1.0
 */
public class MentionableSelectMenu implements SelectMenu {

    private String customId;
    private String placeholder;
    private int minValues;
    private int maxValues;
    private boolean disabled;

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
    public MentionableSelectMenu(String customId, String placeholder, int minValues, int maxValues, boolean disabled) {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.disabled = disabled;
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
                customId, placeholder, minValues, maxValues, disabled
        );
    }

    @Override
    public ComponentType type() {
        return ComponentType.MENTIONABLE_SELECT;
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
        return obj;
    }

    public static MentionableSelectMenu decompile(JSONObject json) {
        String customId = json.has("custom_id") ? json.getString("custom_id") : null;
        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        int minValues = json.has("min_values") ? json.getInt("min_values") : 0;
        int maxValues = json.has("max_values") ? json.getInt("max_values") : 25;
        boolean disabled = json.has("disabled") && json.getBoolean("disabled");

        return new MentionableSelectMenu(customId, placeholder, minValues, maxValues, disabled);
    }
}
