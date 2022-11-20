package com.seailz.javadiscordwrapper.model.component.select;

import com.seailz.javadiscordwrapper.DiscordJv;
import com.seailz.javadiscordwrapper.core.Compilerable;
import com.seailz.javadiscordwrapper.model.emoji.Emoji;
import org.json.JSONObject;

/**
 * Represents a select option
 *
 * @author Seailz
 * @since  1.0
 * @see   com.seailz.javadiscordwrapper.model.interaction.data.message.MessageComponentInteractionData
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
     * @param label The label of the option
     * @param value The value of the option
     */
    public SelectOption(String label, String value) {
        this.label = label;
        this.value = value;
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

    public static SelectOption decompile(JSONObject obj, DiscordJv discordJv) {
        SelectOption option = new SelectOption(obj.getString("label"), obj.getString("value"));
        if (obj.has("description")) {
            option.setDescription(obj.getString("description"));
        }
        if (obj.has("emoji")) {
            option.setEmoji(Emoji.decompile(obj.getJSONObject("emoji"), discordJv));
        }
        if (obj.has("default")) {
            option.setDefaultSelected(obj.getBoolean("default"));
        }
        return option;
    }
}
