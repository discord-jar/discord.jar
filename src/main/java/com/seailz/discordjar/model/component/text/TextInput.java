package com.seailz.discordjar.model.component.text;

import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.ModalComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/**
 * Represents a text input component.
 * This component is only available in {@link com.seailz.discordjar.model.interaction.modal.Modal Modals}
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.component.ModalComponent
 * @since 1.0
 */
public class TextInput implements ModalComponent {

    private String customId;
    private TextInputStyle style;
    private String label;
    private String placeholder;
    private String value;
    private int minLength;
    private int maxLength;
    private boolean required;

    /**
     * Creates a new text input component
     *
     * @param customId    The custom id of the component (max 100 characters)
     * @param style       The {@link TextInputStyle} of the component
     * @param label       The label of the component
     * @param placeholder The placeholder of the component
     * @param value       The pre-filled value of the component
     * @param minLength   The minimum length of the input (min 0, max 4000), default 0
     * @param maxLength   The maximum length of the input (min 1, max 4000), default 4000
     */
    public TextInput(@NotNull String customId, @NotNull TextInputStyle style, @NotNull String label, @Nullable String placeholder, @Nullable String value, int minLength, int maxLength, boolean required) {
        this.customId = customId;
        this.style = style;
        this.label = label;
        this.placeholder = placeholder;
        this.value = value;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    public TextInput(@NotNull String customId, @NotNull TextInputStyle style, @NotNull String label) {
        this(customId, style, label, null, null, 0, 4000, false);
    }

    /**
     * Compiles the component into a {@link JSONObject}
     *
     * @return The compiled component
     */
    @NotNull
    @Override
    public JSONObject compile() {
        if (maxLength < 1)
            throw new IllegalStateException("Max length must be greater than 1");

        if (maxLength > 4000)
            throw new IllegalStateException("Max length must be less than 4000");

        if (minLength > 4000)
            throw new IllegalStateException("Minimum length must be less than 4000");

        if (minLength < 0)
            throw new IllegalStateException("Minimum length must be greater than 0");

        if (customId.length() > 100)
            throw new IllegalStateException("Custom id must be less than 100 characters");

        if (value != null && value.length() > 4000)
            throw new IllegalStateException("Value must be less than 4000 characters");

        if (placeholder != null && placeholder.length() > 100)
            throw new IllegalStateException("Placeholder must be less than 100 characters");

        JSONObject object = new JSONObject();
        object.put("type", type().getCode());
        object.put("custom_id", customId);
        object.put("style", style.code());
        object.put("label", label);
        if (placeholder != null) object.put("placeholder", placeholder);
        if (value != null) object.put("value", value);
        object.put("min_length", minLength);
        object.put("max_length", maxLength);
        object.put("required", required);
        return object;
    }

    /**
     * @param json The json to parse
     * @return The parsed {@link TextInput}
     */
    public static TextInput decompile(@NotNull JSONObject json) {
        String customId = json.getString("custom_id");
        TextInputStyle style = TextInputStyle.fromCode(json.getInt("style"));
        String label = json.getString("label");

        String placeholder = json.has("placeholder") ? json.getString("placeholder") : null;
        String value = json.has("value") ? json.getString("value") : null;
        int minLength = json.has("min_length") ? json.getInt("min_length") : 0;
        int maxLength = json.has("max_length") ? json.getInt("max_length") : 4000;
        boolean required = json.has("required") && json.getBoolean("required");

        return new TextInput(customId, style, label, placeholder, value, minLength, maxLength, required);
    }

    /**
     * @return The custom id of the component
     */
    @NotNull
    @Override
    public String customId() {
        return customId;
    }

    /**
     * @return The {@link ComponentType} of the component
     */
    @NotNull
    @Override
    public ComponentType type() {
        return ComponentType.TEXT_INPUT;
    }

    /**
     * @return if the component is a select menu
     */
    @Override
    public boolean isSelect() {
        return false;
    }

    public TextInput setCustomId(@NotNull String customId) {
        this.customId = customId;
        return this;
    }

    public TextInput setStyle(@NotNull TextInputStyle style) {
        this.style = style;
        return this;
    }

    public TextInput setLabel(@NotNull String label) {
        this.label = label;
        return this;
    }

    public TextInput setPlaceholder(@NotNull String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public TextInput setValue(@NotNull String value) {
        this.value = value;
        return this;
    }

    public TextInput setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public TextInput setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public TextInput setRequired(boolean required) {
        this.required = required;
        return this;
    }

    @Nullable
    public TextInputStyle style() {
        return style;
    }

    @Nullable
    public String label() {
        return label;
    }

    @Nullable
    public String placeholder() {
        return placeholder;
    }

    @Nullable
    public String value() {
        return value;
    }

    public int minLength() {
        return minLength;
    }

    public int maxLength() {
        return maxLength;
    }

    @Override
    public boolean required() {
        return required;
    }
}
