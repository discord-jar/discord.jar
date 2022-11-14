package com.seailz.javadiscordwrapper.model.component.select.string;

import com.seailz.javadiscordwrapper.model.component.ActionComponent;
import com.seailz.javadiscordwrapper.model.component.ComponentType;
import com.seailz.javadiscordwrapper.model.component.select.SelectMenu;
import com.seailz.javadiscordwrapper.model.component.select.SelectOption;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a string select menu
 *
 * @author Seailz
 * @since  1.0
 * @see    com.seailz.javadiscordwrapper.model.component.select.SelectMenu
 */
public class StringSelectMenu implements SelectMenu {

    private String customId;
    private String placeholder;
    private int minValues;
    private int maxValues;
    private SelectOption[] options;

    /**
     * Creates a new string select menu
     * @param customId The custom id of the select menu
     * @param options The options of the select menu
     */
    public StringSelectMenu(String customId, List<SelectOption> options) {
        this.customId = customId;
        this.options = options.toArray(new SelectOption[0]);
    }

    /**
     * Creates a new string select menu
     * @param customId The custom id of the select menu
     * @param placeholder The placeholder of the select menu
     * @param minValues The minimum amount of values that can be selected
     * @param maxValues The maximum amount of values that can be selected
     * @param options The options of the select menu
     */
    public StringSelectMenu(String customId, String placeholder, int minValues, int maxValues, List<SelectOption> options) {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.options = options.toArray(new SelectOption[0]);
    }

    /**
     * Creates a new string select menu
     * @param customId The custom id of the select menu
     * @param selectOptions The options of the select menu
     */
    public StringSelectMenu(String customId, SelectOption... selectOptions) {
        this(customId, new ArrayList<>(List.of(selectOptions)));
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

    public void setOptions(SelectOption[] options) {
        this.options = options;
    }

    public SelectOption[] options() {
        return options;
    }

    public void addOption(SelectOption option) {
        SelectOption[] newOptions = new SelectOption[options.length + 1];
        System.arraycopy(options, 0, newOptions, 0, options.length);
        newOptions[newOptions.length - 1] = option;
        options = newOptions;
    }



    @Override
    public ActionComponent setDisabled(boolean disabled) {
        return new StringSelectMenu(
                customId, placeholder, minValues, maxValues, List.of(options)
        );
    }

    @Override
    public ComponentType type() {
        return ComponentType.STRING_SELECT;
    }

    @Override
    public boolean isSelect() {
        return true;
    }

    @Override
    public JSONObject compile() {
        JSONArray options = new JSONArray();
        for (SelectOption option : this.options)
            options.put(option.compile());

        return new JSONObject()
                .put("type", type().getCode())
                .put("custom_id", customId)
                .put("placeholder", placeholder)
                .put("min_values", minValues)
                .put("max_values", maxValues)
                .put("disabled", isDisabled())
                .put("options", options);
    }

    public static StringSelectMenu decompile(JSONObject json) {
        String customId = json.has("custom_id") ? json.getString("custom_id") : null;
        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        int minValues = json.has("min_values") ? json.getInt("min_values") : 0;
        int maxValues = json.has("max_values") ? json.getInt("max_values") : 25;
        List<SelectOption> options = new ArrayList<>();

        if (json.has("options")) {
            JSONArray optionsJson = json.getJSONArray("options");
            for (int i = 0; i < optionsJson.length(); i++)
                options.add(SelectOption.decompile(optionsJson.getJSONObject(i)));
        }

        return new StringSelectMenu(customId, placeholder, minValues, maxValues, options);
    }

}
