package com.seailz.javadiscordwrapper.model.interaction.data.modal;

import com.seailz.javadiscordwrapper.model.component.Component;
import com.seailz.javadiscordwrapper.model.component.ModalComponent;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModalSubmitInteractionData extends InteractionData {

    private String customId;
    private List<ModalComponent> components;

    public ModalSubmitInteractionData(JSONObject obj) {
        this.customId = obj.has("custom_id") ? obj.getString("custom_id") : null;
        for (Component c : Component.decompileList(obj.getJSONArray("components"))) {
            components = new ArrayList<>();
            components.add((ModalComponent) c);
        }
    }

    public String customId() {
        return customId;
    }

    public List<ModalComponent> components() {
        return components;
    }

    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("custom_id", customId);
        obj.put("components", Component.compileModalList(components));
        return obj;
    }

}
