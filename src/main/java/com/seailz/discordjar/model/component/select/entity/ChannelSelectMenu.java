package com.seailz.discordjar.model.component.select.entity;

import com.seailz.discordjar.events.model.interaction.select.entity.ChannelSelectMenuInteractionEvent;
import com.seailz.discordjar.model.channel.utils.ChannelType;
import com.seailz.discordjar.model.component.ActionComponent;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.select.SelectMenu;
import com.seailz.discordjar.utils.registry.components.ChannelSelectRegistry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a channel select menu
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.select.SelectMenu
 * @since 1.0
 */
public class ChannelSelectMenu implements SelectMenu {

    private String customId;
    private String placeholder;
    private int minValues;
    private int maxValues;
    private List<ChannelType> channelTypes;
    private boolean disabled;

    private JSONObject raw;

    /**
     * Creates a new channel select menu
     *
     * @param customId The custom id of the select menu
     */
    public ChannelSelectMenu(String customId) {
        this.customId = customId;
    }

    /**
     * Creates a new channel select menu
     *
     * @param customId     The custom id of the select menu
     * @param placeholder  The placeholder of the select menu
     * @param minValues    The minimum amount of values that can be selected
     * @param maxValues    The maximum amount of values that can be selected
     * @param channelTypes The channel types that can be selected
     */
    public ChannelSelectMenu(String customId, String placeholder, int minValues, int maxValues, List<ChannelType> channelTypes, boolean disabled, JSONObject raw) {
        this.customId = customId;
        this.placeholder = placeholder;
        this.minValues = minValues;
        this.maxValues = maxValues;
        this.channelTypes = channelTypes;
        this.disabled = disabled;
        this.raw = raw;
    }

    /**
     * Creates a new channel select menu
     *
     * @param customId     The custom id of the select menu
     * @param placeholder  The placeholder of the select menu
     * @param minValues    The minimum amount of values that can be selected
     * @param maxValues    The maximum amount of values that can be selected
     * @param channelTypes The channel types that can be selected
     */
    public ChannelSelectMenu(String customId, String placeholder, int minValues, int maxValues, boolean disabled, JSONObject raw, ChannelType... channelTypes) {
        this(customId, placeholder, minValues, maxValues, List.of(channelTypes), disabled, raw);
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

    public ChannelSelectMenu setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    public ChannelSelectMenu setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public ChannelSelectMenu setMinValues(int minValues) {
        this.minValues = minValues;
        return this;
    }

    public ChannelSelectMenu setMaxValues(int maxValues) {
        this.maxValues = maxValues;
        return this;
    }

    public ChannelSelectMenu setChannelTypes(ChannelType... channelTypes) {
        return setChannelTypes(List.of(channelTypes));
    }

    public ChannelSelectMenu setChannelTypes(List<ChannelType> channelTypes) {
        this.channelTypes = channelTypes;
        return this;
    }

    @Override
    public ActionComponent setDisabled(boolean disabled) {
        return new ChannelSelectMenu(
                customId, placeholder, minValues, maxValues, disabled, raw, channelTypes.toArray(new ChannelType[0])
        );
    }

    /**
     * Sets the action of the select.
     *
     * @param action The action you want to set.
     */
    public ChannelSelectMenu setAction(Consumer<ChannelSelectMenuInteractionEvent> action) {
        ChannelSelectRegistry.getInstance().register(new ChannelSelectMenu.ChannelSelectAction(this, action));
        return this;
    }

    @Override
    public ComponentType type() {
        return ComponentType.CHANNEL_SELECT;
    }

    @Override
    public boolean isSelect() {
        return true;
    }

    @Override
    public JSONObject compile() {
        JSONArray channelTypes = new JSONArray();
        this.channelTypes.forEach(channelType -> channelTypes.put(channelType.getCode()));
        if (minValues > maxValues)
            throw new IllegalArgumentException("Min values cannot be greater than max values");

        JSONObject obj = new JSONObject();
        obj.put("type", type().getCode());
        obj.put("custom_id", customId);
        if (placeholder != null) obj.put("placeholder", placeholder);
        if (minValues != 0) obj.put("min_values", minValues);
        if (maxValues != 0) obj.put("max_values", maxValues);
        if (disabled) obj.put("disabled", true);
        if (this.channelTypes != null) obj.put("channel_types", channelTypes);
        return obj;
    }

    public static ChannelSelectMenu decompile(JSONObject json) {
        String customId = json.has("custom_id") ? json.getString("custom_id") : null;
        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        int minValues = json.has("min_values") ? json.getInt("min_values") : 0;
        int maxValues = json.has("max_values") ? json.getInt("max_values") : 25;
        JSONArray channelTypes = json.has("channel_types") ? json.getJSONArray("channel_types") : null;
        boolean disabled = json.has("disabled") && json.getBoolean("disabled");

        List<ChannelType> channelTypesDecompiled = new ArrayList<>();
        if (channelTypes != null) {
            channelTypes.forEach(channelType -> {
                ChannelType type = ChannelType.fromCode((int) channelType);
                if (type != null) {
                    channelTypesDecompiled.add(type);
                }
            });
        }

        return new ChannelSelectMenu(customId, placeholder, minValues, maxValues, channelTypesDecompiled, disabled, json);
    }

    @Override
    public JSONObject raw() {
        return raw;
    }

    @Override
    public void setRaw(JSONObject raw) {
        this.raw = raw;
    }

    public record ChannelSelectAction(ChannelSelectMenu menu, Consumer<ChannelSelectMenuInteractionEvent> action) {}

}

