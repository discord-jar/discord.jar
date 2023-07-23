package com.seailz.discordjar.model.interaction.reply;

import com.seailz.discordjar.model.component.DisplayComponent;
import com.seailz.discordjar.model.component.RawComponent;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link com.seailz.discordjar.model.interaction.callback.InteractionCallbackType InteractionCallbackType} 9 interaction response.
 * This includes modal info.
 * <p>
 * Note: This is an internal class and should not be used by the end user.
 *
 * @author Seailz
 * @see InteractionReply
 * @since 1.0
 */
public class InteractionModalResponse implements InteractionReply {

    private String title;
    private String customId;
    private List<DisplayComponent> components;

    public InteractionModalResponse(String title, String customId, List<DisplayComponent> components) {
        this.title = title;
        this.customId = customId;
        this.components = components;
    }

    public InteractionModalResponse(String title, String customId, DisplayComponent... components) {
        this.title = title;
        this.customId = customId;
        this.components = List.of(components);
    }

    public InteractionModalResponse(String title, String customId) {
        this.title = title;
        this.customId = customId;
        components = new ArrayList<>();
    }

    public void addComponent(DisplayComponent component) {
        this.components.add(component);
    }

    public void addComponents(DisplayComponent... components) {
        this.components.addAll(List.of(components));
    }

    public String title() {
        return title;
    }

    public String customId() {
        return customId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public void removeComponent(DisplayComponent component) {
        this.components.remove(component);
    }

    public void removeComponents(DisplayComponent... components) {
        this.components.removeAll(List.of(components));
    }

    public void setComponents(List<DisplayComponent> components) {
        this.components = components;
    }

    public void setComponents(DisplayComponent... components) {
        this.components = List.of(components);
    }

    @Override
    public SJSONObject compile() {
        if (components.size() < 1)
            throw new IllegalStateException("Modal must have at least 1 component");

        if (components.size() > 5)
            throw new IllegalStateException("Modal must have no more than 5 components");

        if (title.length() > 45)
            throw new IllegalStateException("Modal title must be no more than 45 characters");

        if (customId.length() > 100)
            throw new IllegalStateException("Modal custom id must be no more than 100 characters");

        for (DisplayComponent component : components) {
            for (RawComponent com : component.components()) {
                if (!com.isModalCompatible())
                    throw new IllegalStateException("Component " + com.getClass().getSimpleName() + " is not compatible with a modal");
            }
        }

        SJSONObject json = new SJSONObject();
        json.put("title", title);

        SJSONArray components = new SJSONArray();
        for (DisplayComponent component : this.components) {
            components.put(component.compile());
        }

        json.put("custom_id", customId);
        json.put("components", components);
        return json;
    }

    @Override
    public boolean useFiles() {
        return false;
    }

    @Override
    public List<File> getFiles() {
        return null;
    }
}
