package com.seailz.discordjar.model.component.button.internal;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.events.model.interaction.button.ButtonInteractionEvent;
import com.seailz.discordjar.model.component.ComponentType;
import com.seailz.discordjar.model.component.button.Button;
import com.seailz.discordjar.model.component.button.ButtonStyle;
import com.seailz.discordjar.model.emoji.Emoji;
import com.seailz.discordjar.utils.registry.ButtonRegistry;
import org.json.JSONObject;

import java.util.function.Consumer;

public class ButtonImpl implements Button {

    private String label;
    private ButtonStyle style;
    private Emoji emoji;
    private String customId;
    private String url;
    private boolean isDisabled;

    @Override
    public JSONObject compile() {
        JSONObject json = new JSONObject();
        json.put("type", ComponentType.BUTTON.getCode());
        json.put("label", label);
        json.put("style", style.code());
        json.put("custom_id", customId);
        if (url != null) json.put("url", url);
        if (isDisabled) json.put("disabled", true);
        if (emoji != null) json.put("emoji", emoji.compile());
        return json;
    }

    @Override
    public String customId() {
        return customId;
    }

    @Override
    public Button setDisabled(boolean disabled) {
        isDisabled = disabled;
        return this;
    }

    @Override
    public Button setCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    @Override
    public Button setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public Button setStyle(ButtonStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public Button setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    @Override
    public Button setUrl(String url) {
        if (style != ButtonStyle.LINK)
            throw new IllegalStateException("Cannot set a url on a button that is not a link button.");
        this.url = url;
        return this;
    }

    @Override
    public Button setAction(Consumer<ButtonInteractionEvent> action) {
        ButtonRegistry.getInstance().register(new ButtonAction(this, action));
        return this;
    }

    @Override
    public ComponentType type() {
        return ComponentType.BUTTON;
    }

    @Override
    public boolean isSelect() {
        return false;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public ButtonStyle style() {
        return style;
    }

    @Override
    public Emoji emoji() {
        return emoji;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public String url() {
        return url;
    }

    public static Button decompile(JSONObject obj, DiscordJar discordJar) {
        ButtonImpl button = new ButtonImpl();
        button.setLabel(obj.getString("label"));
        button.setStyle(ButtonStyle.fromCode(obj.getInt("style")));
        button.setCustomId(obj.getString("custom_id"));
        if (obj.has("url")) button.setUrl(obj.getString("url"));
        if (obj.has("disabled")) button.setDisabled(obj.getBoolean("disabled"));
        if (obj.has("emoji")) button.setEmoji(Emoji.decompile(obj.getJSONObject("emoji"), discordJar));
        return button;
    }
}
