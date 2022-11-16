package com.seailz.javadiscordwrapper.model.interaction.data.modal;

import com.seailz.javadiscordwrapper.model.component.Component;
import com.seailz.javadiscordwrapper.model.component.ModalComponent;
import com.seailz.javadiscordwrapper.model.component.modal.ResolvedModalComponent;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalSubmitInteractionData extends InteractionData {

    private String customId;
    private List<ResolvedModalComponent> components;

    public ModalSubmitInteractionData(JSONObject obj) {
        this.customId = obj.has("custom_id") ? obj.getString("custom_id") : null;

        List<ResolvedModalComponent> resolvedModalComponents = new ArrayList<>();
        JSONArray com = obj.getJSONArray("components");
        for (Object o : com) {
            JSONObject json = (JSONObject) o;
            JSONArray components = json.getJSONArray("components");
            for (Object comp : components) {
                JSONObject jsonComp = (JSONObject) comp;
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
