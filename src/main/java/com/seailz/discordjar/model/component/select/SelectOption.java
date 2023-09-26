package com.seailz.discordjar.model.component.select;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.core.Compilerable;
import com.seailz.discordjar.model.emoji.Emoji;
import org.json.JSONObject;

/**
 * Represents a select option
 *
 * @author Seailz
 * @see com.seailz.discordjar.model.interaction.data.message.MessageComponentInteractionData
 * @since 1.0
 */
public class SelectOption implements Compilerable {

    // The label of the option
    private String label;
    // The custom value of the option
    private String value;
    // The description of the option
    private String description;
    // The emoji of the option
    private Emoji emoji;
    // Whether the option is default
    private boolean defaultSelected;

    /**
     * Creates a new select option
     *
     * @param label The label of the option
     * @param value The value of the option
     */
    public SelectOption(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public static SelectOption decompile(JSONObject obj, DiscordJar discordJar) {
        SelectOption option = new SelectOption(obj.getString("label"), obj.getString("value"));
        if (obj.has("description")) {
            option.setDescription(obj.getString("description"));
        }
        if (obj.has("emoji")) {
            option.setEmoji(Emoji.decompile(obj.getJSONObject("emoji"), discordJar));
        }
        if (obj.has("default")) {
            option.setDefaultSelected(obj.getBoolean("default"));
        }
        return option;
    }

    public String label() {
        return label;
    }

    public String value() {
        return value;
    }

    public String description() {
        return description;
    }

    public Emoji emoji() {
        return emoji;
    }

    public boolean defaultSelected() {
        return defaultSelected;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    public void setDefaultSelected(boolean defaultSelected) {
        this.defaultSelected = defaultSelected;
    }

    @Override
    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("label", label);
        obj.put("value", value);
        if (description != null) {
            obj.put("description", description);
        }
        if (emoji != null) {
            obj.put("emoji", emoji.compile());
        }
        if (defaultSelected) {
            obj.put("default", true);
        }
        return obj;
    }

    public record ResolvedSelectOption(String value) {
        public static ResolvedSelectOption decompile(String value) {
            return new ResolvedSelectOption(value);
        }
    }
}
