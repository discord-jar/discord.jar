package com.seailz.discordjar.model.interaction.data.modal;

import com.seailz.discordjar.model.component.modal.ResolvedModalComponent;
import com.seailz.discordjar.model.interaction.InteractionData;
import com.seailz.discordjar.utils.json.SJSONArray;
import com.seailz.discordjar.utils.json.SJSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalSubmitInteractionData extends InteractionData {

    private final String customId;
    private final List<ResolvedModalComponent> components;

    public ModalSubmitInteractionData(SJSONObject obj) {
        this.customId = obj.has("custom_id") ? obj.getString("custom_id") : null;

        List<ResolvedModalComponent> resolvedModalComponents = new ArrayList<>();
        SJSONArray com = obj.getJSONArray("components");
        for (Object o : com) {
            SJSONObject json = (SJSONObject) o;
            SJSONArray components = json.getJSONArray("components");
            for (Object comp : components) {
                SJSONObject jsonComp = (SJSONObject) comp;
                ResolvedModalComponent decompiled = ResolvedModalComponent.decompile(jsonComp);
                resolvedModalComponents.add(decompiled);
            }
        }
        this.components = resolvedModalComponents;
    }

    public String customId() {
        return customId;
    }

    public List<ResolvedModalComponent> components() {
        return components;
    }

}
