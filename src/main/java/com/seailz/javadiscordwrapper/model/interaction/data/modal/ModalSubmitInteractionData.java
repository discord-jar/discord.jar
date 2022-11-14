package com.seailz.javadiscordwrapper.model.interaction.data.modal;

import com.seailz.javadiscordwrapper.model.component.Component;
import com.seailz.javadiscordwrapper.model.interaction.InteractionData;
import org.json.JSONObject;

import java.util.List;

public class ModalSubmitInteractionData extends InteractionData {

    private String customId;
    private List<Component> components;

    public ModalSubmitInteractionData(JSONObject obj) {
        this.customId = obj.has("custom_id") ? obj.getString("custom_id") : null;
        this.components = obj.has("components") ? Component.decompileList(obj.getJSONArray("components")) : null;
    }

    public String customId() {
        return customId;
    }

    public List<Component> components() {
        return components;
    }

    public JSONObject compile() {
        JSONObject obj = new JSONObject();
        obj.put("custom_id", customId);
        obj.put("components", Component.compileList(components));
        return obj;
    }

}
